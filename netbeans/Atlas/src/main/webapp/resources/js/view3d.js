// jQuery, three.js and OrbitControls.js must be loaded for this code to work!

// basic 3D world objects
var width, height, container;
var renderer, camera, scene, controls;
var pointLight, ambientLight, lightOffset;
var material1, lineMaterial, markMaterial;
var sprites;
var projector, mouseVector;
var textBox, textBoxContent, defaultMessage;

// currently selected label
var selectedLabel = null;

// initialize
init();
// render (loop not needed, render should be called by controls)
render();

// initialize 3D world
// everything that happens before first render goes here
function init() {
    // get containter element for canvas
    container = document.getElementById('container'); //container = $('#container');
    
    // set the scene size depending on container
    //width = container.offsetWidth; //width = container.width();
    //height = width * 0.75;
    //alert(width + "x" + height);

    // create a scene
    scene = new THREE.Scene();
    // container for sprites
    sprites = new THREE.Object3D();
    scene.add( sprites );
    // create a camera
    camera = new THREE.PerspectiveCamera(45, 0.75, 1, 10000);
    // add the camera to the scene
    scene.add(camera);
    // pull back the camera
    camera.position.z = 200;

    // create renderer
    renderer = new THREE.WebGLRenderer();
    //renderer.setSize(width, height);
    renderer.setClearColor( 0xF0F0F0, 0 );
    // append renderer element to container
    container.appendChild(renderer.domElement);
    // setup orbit controls
    controls = new THREE.OrbitControls( camera, renderer.domElement );
    controls.addEventListener( 'change', render );

    // create a materials
    material1 = new THREE.MeshLambertMaterial( {color: 0xFFFF99} );
    lineMaterial = new THREE.LineBasicMaterial({
        vertexColors: THREE.VertexColors,
        color: 0xFFFFFF });
    markMaterial = new THREE.MeshBasicMaterial( {color: 0xFF0000} );

    // create cube for testing purposes
    //var cube = new THREE.Mesh( new THREE.CubeGeometry(100, 100, 100), material1 );
    // add the cube to the scene
    //scene.add(cube);

    // create a point light
    pointLight = new THREE.PointLight(0xFFFFFF);
    // set offset from camera position (used on render)
    lightOffset = new THREE.Vector3(300, 300, 300);
    // create an embient light
    ambientLight = new THREE.AmbientLight( 0x333333 );
    // add lights to the scene
    scene.add(pointLight);
    scene.add(ambientLight);
    
    // picking
    projector = new THREE.Projector();
    mouseVector = new THREE.Vector3( 0, 0, 1);
    
    // listeners
    window.addEventListener( 'click', onMouseClick, false );
    window.addEventListener( 'resize', onWindowResize, false );
    document.addEventListener("DOMContentLoaded", function() {
        onWindowResize();
    });
    
    // test textbox
    setupTextBox();

    // load 3D model
    //alert(modelPath);
    loadModel( modelPath );
    
    // create labels
    labels.forEach( function ( label ) {
        makeLabel(label.title, label.text,
            new THREE.Vector3(label.markX, label.markY, label.markZ), 
            new THREE.Vector3(label.labelX, label.labelY, label.labelZ));
    });
}

function loadModel( path ) {
    var loader = new THREE.STLLoader();
    loader.addEventListener( 'load', function ( event ) {
        // get geometry
        var geometry = event.content;
        // get bounding box for normalization
        geometry.computeBoundingBox();
        // shift to center
        var shift = geometry.boundingBox.min.clone();
        shift.add( geometry.boundingBox.max );
        shift.divideScalar( -2 );
        // scale longest dimension to 100
        var dim = geometry.boundingBox.max.clone();
        dim.sub( geometry.boundingBox.min );
        var scale = 100 / Math.max( dim.x, dim.y, dim.z );
        // create mesh with material
        var model = new THREE.Mesh( geometry, material1 );
        model.name = "model";
        // apply transforms
        model.scale.set( scale, scale, scale );
        model.translateX( shift.x * scale );
        model.translateY( shift.y * scale );
        model.translateZ( shift.z * scale );
                
        // add model and render
        scene.add( model );
        render();
    } );
    loader.load( path );
    
}

function setupTextBox() {
    
    defaultMessage = "Select labels for details";
    
    var padding = 10;
    
    var holderDiv = document.createElement( 'div' );
    holderDiv.style.position = 'absolute';
    //holderDiv.style.width = w + 'px';
    //holderDiv.style.maxHeight = h + 'px';
    holderDiv.style.backgroundColor = "rgba(0, 0, 0, 0.8)";
    holderDiv.style.borderRadius = 10 + 'px';
    //holderDiv.style.bottom = 0 + 'px';
    //holderDiv.style.left = 0 + 'px';
    //alert(container.style.bottom);
    //holderDiv.style.top = container.offsetTop + height - h - 5 + 'px';
    //holderDiv.style.left = container.offsetLeft + 0.5*( width - w ) + 'px';
    //holderDiv.style.display = 'table';
    holderDiv.style.overflowY = 'auto';
    
    var textDiv = document.createElement( 'div' );
    textDiv.style.color = 'rgba(255, 255, 255, 1)';
    textDiv.style.padding = padding + 'px';
    textDiv.style.textAlign = 'left';
    //textDiv.style.display = 'table-cell';
    //textDiv.style.verticalAlign = 'middle';
    textDiv.innerHTML = defaultMessage;
    
    holderDiv.appendChild( textDiv );
    container.appendChild( holderDiv );
    textBox = holderDiv;
    textBoxContent = textDiv;
}


function makeLabel( title, text, markPosition, labelPosition ) {
    makeSprite( title, text, labelPosition );
    makeLine( markPosition, labelPosition );
    makeMark( markPosition );
}

function makeSprite( title, text, position ) {
    
    // create canvas to draw texture on
    var canvas = document.createElement('canvas');
    // grab 2D context
    var context = canvas.getContext('2d');
    // set fontsize
    var fontsize = 50;
    // set horizontal margin
    var margin = 20;
    // calculate text width with current font
    context.font = fontsize + "px Arial";
    var metrics = context.measureText( title );
    var textWidth = metrics.width;
    // texture dimensions
    var hSize = textWidth + 2*margin;
    var vSize = fontsize*1.4;
    // change canvas size to fit our texture
    canvas.width = textWidth + 2*margin;
    // re-set fonts, they changed with canvas size
    context.font = fontsize + "px Arial";
    // set fill and border color
    context.fillStyle = "rgba( 200, 200, 200, 0.8 )";
    context.strokeStyle = "rgba( 0, 0, 0, 1.0 )";
    // draw rounded rectangle
    roundRect(context, 0, 0, hSize, vSize, 15);
    
    context.fillStyle = "rgba(0,0,0,255)";
    context.fillText(title, margin, fontsize);
    

    //// use canvas contents as a texture
    var texture = new THREE.Texture(canvas);
        texture.needsUpdate = true;

    var material = new THREE.MeshBasicMaterial( { map: texture, transparent: true } );
    
    var geometry = new THREE.PlaneGeometry( hSize/vSize, 1 );
    // setup UVs
    var coords = [new THREE.Vector2(0, 1-vSize/canvas.height),
                  new THREE.Vector2(hSize/canvas.width, 1-vSize/canvas.height),
                  new THREE.Vector2(hSize/canvas.width, 1),
                  new THREE.Vector2(0, 1)];
    geometry.faceVertexUvs[0] = [];
    geometry.faceVertexUvs[0][0] = [coords[3], coords[0], coords[2]];
    geometry.faceVertexUvs[0][1] = [coords[0], coords[1], coords[2]];
    
    var sprite = new THREE.Mesh( geometry, material );
    
    // replace new lines with <br />
    var breakTag = '<br />';
    sprite.name = title.replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');    
    sprite.text = text.replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');

    sprite.scale.set( 2, 2, 1);
    sprite.position.copy( position );

    sprites.add(sprite);	
}

function makeLine( start, end ) {
    
    // a bit shorter looks better? best would be to always end at label edge
    var ratio = 0.9;
    var newEnd = new THREE.Vector3();
    newEnd.subVectors( start, end ).multiplyScalar( 1 - ratio );
    newEnd.add( end );
    // add midpoint for better coloring
    var mid = new THREE.Vector3();
    mid.addVectors( start, newEnd ).multiplyScalar( 0.5 );
    
    var geometry = new THREE.Geometry();
    
    geometry.vertices.push( start, mid, newEnd );
    geometry.colors[0] = markMaterial.color;
    geometry.colors[1] = markMaterial.color;
    geometry.colors[2] = renderer.getClearColor();
    geometry.colorsNeedUpdate = true;
    var line = new THREE.Line( geometry, lineMaterial );

    scene.add( line );
}

function makeMark( position ) {
    
    var geometry = new THREE.SphereGeometry( 0.5, 16, 16 );
    var mark = new THREE.Mesh( geometry, markMaterial );
    
    mark.position.copy( position );
    scene.add( mark );
    
}


// function for drawing rounded rectangles
// fill and stroke properties should be set prior to call
function roundRect( context, x, y, w, h, r ) {

    context.beginPath();
    context.moveTo(x+r, y);
    context.lineTo(x+w-r, y);
    context.quadraticCurveTo(x+w, y, x+w, y+r);
    context.lineTo(x+w, y+h-r);
    context.quadraticCurveTo(x+w, y+h, x+w-r, y+h);
    context.lineTo(x+r, y+h);
    context.quadraticCurveTo(x, y+h, x, y+h-r);
    context.lineTo(x, y+r);
    context.quadraticCurveTo(x, y, x+r, y);
    context.closePath();
    context.fill();
    //context.stroke(); // border if wanted
    
}

function select( label ) {

    if ( selectedLabel !== null ) {
        selectedLabel.material.color.setHex( 0xFFFFFF );
    }
    
    if ( selectedLabel === label ) {
        selectedLabel = null;
        textBoxContent.innerHTML = defaultMessage;
    } else {
        selectedLabel = label;
        selectedLabel.material.color.setHex( 0xFF3030 );
        
        textBoxContent.innerHTML =
                "<h3>" + label.name + "</h3><p>" + label.text + "</p>";
    }

}

function onMouseClick( event ) {
    var mouse = { x: 0, y: 0 };
    //mouse.x = ( event.clientX / window.innerWidth ) * 2 - 1;
    //mouse.y = - ( event.clientY / window.innerHeight ) * 2 + 1;
    
    // get scrolling offsets - with IE compatibility...
    var scrollX = (window.pageXOffset !== undefined) ? window.pageXOffset :
            (document.documentElement || document.body.parentNode || document.body).scrollLeft;
    var scrollY = (window.pageYOffset !== undefined) ? window.pageYOffset :
        (document.documentElement || document.body.parentNode || document.body).scrollTop;
    
    // mouse.x = ( ( event.clientX - renderer.domElement.offsetLeft + scrollX ) / renderer.domElement.width ) * 2 - 1;
    // mouse.y = - ( ( event.clientY - renderer.domElement.offsetTop + scrollY ) / renderer.domElement.height ) * 2 + 1;
    
    mouse.x = ( ( event.clientX - renderer.domElement.offsetLeft + scrollX ) / width ) * 2 - 1;
    mouse.y = - ( ( event.clientY - renderer.domElement.offsetTop + scrollY ) / height ) * 2 + 1;
    
    var vector = new THREE.Vector3( mouse.x, mouse.y, 1 );
	projector.unprojectVector( vector, camera );
	var ray = new THREE.Raycaster( camera.position, vector.sub( camera.position ).normalize() );
    
//    var raycaster = projector.pickingRay( mouseVector.clone(), camera );
//    var intersects = raycaster.intersectObjects( scene, true );
    var intersects = ray.intersectObjects( sprites.children );
    
    if ( intersects.length > 0) {
        select (intersects[ 0 ].object);
        render();
    }
    
    // alert for "debugging"
    var alertString = "\nClient coords: " + event.clientX + ", " + event.clientY +
            "\nInner width: " + window.innerWidth +
            "\nOuter width: " + window.outerWidth +
            "\nCanvas width: " + renderer.domElement.width +
            "\nCanvas coords: " + mouse.x + ", " + mouse.y +
            "\nMet objects: " + intersects.length;
    for( var i = 0; i < intersects.length; i++ ) {
        alertString = alertString + ", " + intersects[ i ].object.name;
    }
    //alert(alertString);
}

function onWindowResize() {    
    // set dimensions to container size
    width = container.offsetWidth;
    // prefer fixed ratio
    height = 0.75 * width;
    // but fit to window if it would overflow / cause scrolling
    width = Math.min( width, window.innerWidth );
    height = Math.min( height, window.innerHeight );
    // set renderer (canvas) size
    renderer.setSize( width, height );
    // fix camera aspect
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
    
    var w = Math.min( 400, width );
    var h = Math.min( 200, height*0.4 );
    var wMax = width;
    textBox.style.width = w + 'px';
    textBox.style.maxWidth = wMax + 'px';
    textBox.style.maxHeight = h - 15 + 'px';
    //alert(textBox.style.maxHeight);
    textBox.style.bottom = window.innerHeight - height - container.offsetTop + 5 + 'px';
    //holderDiv.style.left = 0 + 'px';
    //alert(container.style.bottom);
    //holderDiv.style.top = container.offsetTop + height - h - 5 + 'px';
    textBox.style.left = container.offsetLeft +
            0.5*( width - Math.min(w, wMax)) + 'px';
    
    render();
}

// render function
// everything that happens every frame goes here
function render() {
    // move point light with camera
    var lightShift = new THREE.Vector3();
    lightShift.copy(lightOffset); // value of offset
    // light position: apply camera transform to offset, add to camera position
    pointLight.position.addVectors(
            lightShift.applyQuaternion( camera.quaternion ), camera.position );
    
    for(var i = 0; i < sprites.children.length; i++) {
        sprites.children[i].quaternion.copy( camera.quaternion );
    }
    
    // draw scene
    renderer.render( scene, camera );
}