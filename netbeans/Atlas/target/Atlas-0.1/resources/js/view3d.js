// jQuery, three.js and OrbitControls.js must be loaded for this code to work!

// basic 3D world objects
var renderer, camera, scene, controls;
var pointLight, ambientLight, lightOffset;
// clock to compensate for different framerates
var clock = new THREE.Clock();

// initialize
init();
// render (loop not needed, render should be called by controls)
render();

// initialize 3D world
// everything that happens before first render goes here
function init() {
    // get containter element for canvas
    var container = $('#container');

    // set the scene size depending on container
    var width = container.width();
    var height = width * 0.75;

    // create a scene
    scene = new THREE.Scene();
    // create a camera
    camera = new THREE.PerspectiveCamera(45, width / height, 1, 10000);
    // add the camera to the scene
    scene.add(camera);
    // pull back the camera
    camera.position.z = 300;

    // create renderer
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(width, height);
    // append renderer element to container
    container.append(renderer.domElement);
    
    // setup orbit controls
    controls = new THREE.OrbitControls( camera, renderer.domElement );
    controls.addEventListener( 'change', render );

    // create a material
    var material1 = new THREE.MeshLambertMaterial( {color: 0xFFFF99} );

    // create cube for testing purposes
    var cube = new THREE.Mesh( new THREE.CubeGeometry(100, 100, 100), material1 );
    // add the cube to the scene
    scene.add(cube);

    // create a point light
    pointLight = new THREE.PointLight(0xFFFFFF);
    // set offset from camera position (used on render)
    lightOffset = new THREE.Vector3(300, 300, 300);
    // create an embient light
    ambientLight = new THREE.AmbientLight( 0x333333 );
    // add lights to the scene
    scene.add(pointLight);
    scene.add(ambientLight);
    
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
    
    // draw scene
    renderer.render( scene, camera );
}