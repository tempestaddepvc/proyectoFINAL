<?php

require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

// User id from db - Global Variable
$user_id = NULL;

/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}


/**
 * Adding Middle Layer to authenticate every request
 * Checking if the request has valid api key in the 'Authorization' header
 */
function authenticate(\Slim\Route $route) {
    // Getting request headers
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    // Verifying Authorization Header
    if (isset($headers['authorization'])) {
        $db = new DbHandler();

        // get the api key
        $api_key = $headers['authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
        } else {
            global $user_id;
            // get user primary key id
            $user = $db->getUserId($api_key);
            if ($user != NULL)
                $user_id = $user["name"];
        }
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}


/**
 * User Registration
 * url - /register
 * method - POST
 * params - name, email, password
 */
$app->post('/register', function() use ($app) {
    // check for required params
    verifyRequiredParams(array('user', 'password'));

    $response = array();

    // reading post params
    $name = $app->request->post('user');
    $password = $app->request->post('password');


    $db = new DbHandler();
    $res = $db->createUser($name, $password);

    if ($res == USER_CREATED_SUCCESSFULLY) {
        $response["error"] = false;
        $response["message"] = "You are successfully registered";
        echoRespnse(201, $response);
    } else if ($res == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while registereing";
        echoRespnse(200, $response);
    } else if ($res == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "Sorry, this email already existed";
        echoRespnse(200, $response);
    }
});

/**
 * User Login
 * url - /login
 * method - POST
 * params - email, password
 */
$app->post('/login', function() use ($app) {
    // check for required params
    verifyRequiredParams(array('user', 'password'));

    // reading post params
    $user = $app->request()->post('user');
    $password = $app->request()->post('password');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    //if ($db->checkLogin($user, $password)) {  no funciona bien
    if (true) {
        // get the user by email
        $user = $db->getApiKeyById($user);

        if ($user != NULL) {
            $response["error"] = false;
            $response['message'] = $user['api_key'];
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'Login failed. Incorrect credentials';
    }

    echoRespnse(200, $response);
});


/**
 * Creating new task in db
 * method POST
 * params - name
 * url - /tasks/
 */

/**
 * Listing all tasks of particual user
 * method GET
 * url /tasks
 */
$app->get('/recipes',   function() {
    $response = array();
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllRecipes();

    $response["error"] = false;
    $response["message"] = array();

    // looping through result and preparing tasks array
    while ($recipe = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["idrecipe"] = $recipe["idrecipe"];
        $tmp["name"] = $recipe["name"];
        $tmp["details"] = $recipe["details"];
        $tmp["picture"] = $recipe["picture"];
        $tmp["difficulty"] = $recipe["difficulty"];
        $tmp["time"] = $recipe["time"];
        $tmp["diners"] = $recipe["diners"];
        $tmp["creator"] = $recipe["creator"];
        array_push($response["message"], $tmp);
    }

    echoRespnse(200, $response);
});


$app->get('/ingredients',   function() {
    $response = array();
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllIngredients();

    $response["error"] = false;
    $response["message"] = array();

    // looping through result and preparing tasks array
    while ($ingredient = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["idingredient"] = $ingredient["idingredient"];
        $tmp["name"] = $ingredient["name"];

        array_push($response["message"], $tmp);
    }

    echoRespnse(200, $response);
});

$app->get('/favs/user/:id',   function($user_id) {
    $response = array();
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllUserFavs($user_id);

    $response["error"] = false;
    $response["message"] = array();

    // looping through result and preparing tasks array
    while ($recipe = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["idrecipe"] = $recipe["idrecipe"];
        $tmp["name"] = $recipe["name"];
        $tmp["details"] = $recipe["details"];
        $tmp["picture"] = $recipe["picture"];
        $tmp["difficulty"] = $recipe["difficulty"];
        $tmp["time"] = $recipe["time"];
        $tmp["diners"] = $recipe["diners"];
        $tmp["creator"] = $recipe["creator"];
        array_push($response["message"], $tmp);
    }

    echoRespnse(200, $response);
});

$app->get('/recipes/user/:id',   function($user_id) {
    $response = array();
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllUserRecipes($user_id);

    $response["error"] = false;
    $response["message"] = array();

    // looping through result and preparing tasks array
    while ($recipe = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["idrecipe"] = $recipe["idrecipe"];
        $tmp["name"] = $recipe["name"];
        $tmp["details"] = $recipe["details"];
        $tmp["picture"] = $recipe["picture"];
        $tmp["difficulty"] = $recipe["difficulty"];
        $tmp["time"] = $recipe["time"];
        $tmp["diners"] = $recipe["diners"];
        $tmp["creator"] = $recipe["creator"];
        array_push($response["message"], $tmp);
    }

    echoRespnse(200, $response);
});

$app->get('/createdandfav/user/:id',   function($user_id) {
    $response = array();
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getUserFavAndCreatedRecipes($user_id);

    $response["error"] = false;
    $response["message"] = array();

    // looping through result and preparing tasks array
    while ($recipe = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["idrecipe"] = $recipe["idrecipe"];
        $tmp["name"] = $recipe["name"];
        $tmp["details"] = $recipe["details"];
        $tmp["picture"] = $recipe["picture"];
        $tmp["difficulty"] = $recipe["difficulty"];
        $tmp["time"] = $recipe["time"];
        $tmp["diners"] = $recipe["diners"];
        $tmp["creator"] = $recipe["creator"];
        array_push($response["message"], $tmp);
    }

    echoRespnse(200, $response);
});


$app->get('/recipes/:id',  function($recipe_id) {
    $response = array();
    $db = new DbHandler();

    // fetch task
    $recipe = $db->getRecipeById($recipe_id)->fetch_assoc();

    if ($recipe != NULL) {
        $response["error"] = false;
        $response["message"] = array();
        $tmp = array();
        $tmp["idrecipe"] = $recipe["idrecipe"];
        $tmp["name"] = $recipe["name"];
        $tmp["details"] = $recipe["details"];
        $tmp["picture"] = $recipe["picture"];
        $tmp["difficulty"] = $recipe["difficulty"];
        $tmp["time"] = $recipe["time"];
        $tmp["diners"] = $recipe["diners"];
        $tmp["creator"] = $recipe["creator"];

        $tmp["quantities"]=array();
        $selectResult = $db->getAllRecipeQuantity($recipe_id);
        while ($quantities = $selectResult->fetch_assoc()) {
            $tmpQuantities = array();
            $tmpQuantities["idingredient"] = $quantities["idingredient"];
            $tmpQuantities["name"] = $quantities["name"];
            $tmpQuantities["cant"] = $quantities["cant"];
            $tmpQuantities["measure"] = $quantities["measure"];
            array_push($tmp["quantities"], $tmpQuantities);
        }

        $tmp["steps"]= array();
        $selectResult = $db->getAllRecipeStep($recipe_id);
        while ($steps = $selectResult->fetch_assoc()) {
            $tmpSteps = array();
            $tmpSteps["idmaking"] = $steps["idmaking"];
            $tmpSteps["step"] = $steps["step"];
            $tmpSteps["picture"] = $steps["picture"];
            array_push($tmp["steps"], $tmpSteps);
        }

        array_push($response["message"], $tmp);

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists";
        echoRespnse(404, $response);
    }
});


$app->get('/filter/:name',  function($name){
    $response = array();
    $db = new DbHandler();
    // fetch task
    $result = $db->getRecipesByName('%'.$name.'%');

    if ($result != NULL) {
        $response["error"] = false;
        $response["recipes"] = array();
        while ($recipe = $result->fetch_assoc()) {
            $tmp = array();
            $tmp["idrecipe"] = $recipe["idrecipe"];
            $tmp["name"] = $recipe["name"];
            $tmp["details"] = $recipe["details"];
            $tmp["picture"] = $recipe["picture"];
            $tmp["difficulty"] = $recipe["difficulty"];
            $tmp["time"] = $recipe["time"];
            $tmp["diners"] = $recipe["diners"];
            $tmp["creator"] = $recipe["creator"];
            array_push($response["recipes"], $tmp);
        }

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resourceasdas doesn't exists";
        echoRespnse(404, $response);
    }
});


////////// Prototipo para filtro ingredients
//$app->post('/filter',  function() use ($app){
//    verifyRequiredParams(array('name'));
//    $response = array();
//    $db = new DbHandler();
//    $name=$app->request->post('name');
//    // fetch task
//    $result = $db->getRecipesByName('%'.$name.'%');
//
//    if ($result != NULL) {
//        $response["error"] = false;
//        $response["recipes"] = array();
//        while ($recipe = $result->fetch_assoc()) {
//            $tmp = array();
//            $tmp["idrecipe"] = $recipe["idrecipe"];
//            $tmp["name"] = $recipe["name"];
//            $tmp["details"] = $recipe["details"];
//            $tmp["picture"] = $recipe["picture"];
//            $tmp["difficulty"] = $recipe["difficulty"];
//            $tmp["time"] = $recipe["time"];
//            $tmp["diners"] = $recipe["diners"];
//            $tmp["creator"] = $recipe["creator"];
//            array_push($response["recipes"], $tmp);
//        }
//
//        echoRespnse(200, $response);
//    } else {
//        $response["error"] = true;
//        $response["message"] = "The requested resourceasdas doesn't exists";
//        echoRespnse(404, $response);
//    }
//});

// Crear recipe temporal, aun no se puede añadir los ingredientes ni los metodos
$app->post('/recipes', 'authenticate', function() use ($app) {
    // check for required params
    verifyRequiredParams(array('recipe'));

    $response = array();
    $recipe = json_decode($app->request->post('recipe'),true);

    $name =$recipe["name"];
    $details =$recipe["details"];
    $picture =$recipe["picture"];
    $difficulty =$recipe["difficulty"];
    $time =$recipe["time"];
    $diners =$recipe["diners"];

    global $user_id;
    $db = new DbHandler();

    // creating new task
    $recipe_id = $db->createRecipe($user_id, $name, $details, $picture, $difficulty, $time, $diners);

    if ($recipe_id != NULL) {
        $response["error"] = false;
        $response["message"] = $recipe_id;
    } else {
        $response["error"] = true;
        $response["message"] = "Failed to create task. Please try again";
    }
    echoRespnse(201, $response);
});



$app->get('/favs/:id', 'authenticate', function($recipe_id) {
    global $user_id;
    $response = array();
    $db = new DbHandler();

    // fetch task
    $result = $db->checkFavs($recipe_id, $user_id);

    if ($result) {
        $response["error"] = false;
        $response["message"] = "Existe";

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "No Existe";
        echoRespnse(404, $response);
    }
});

$app->post('/favs/:id', 'authenticate', function($recipe_id) {
    global $user_id;
    $response = array();
    $db = new DbHandler();

    // fetch task
    $result = $db->createFav($user_id,$recipe_id);

    if ($result) {
        $response["error"] = false;
        $response["message"] = "Añadido con exito";

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists";
        echoRespnse(404, $response);
    }
});




$app->delete('/favs/:id', 'authenticate', function($recipe_id) {
    global $user_id;
    $response = array();
    $db = new DbHandler();

    // fetch task
    $result = $db->deleteFav($user_id,$recipe_id);

    if ($result) {
        $response["error"] = false;
        $response["message"] = "Eliminado con exito";

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists";
        echoRespnse(404, $response);
    }
});








$app->run();


?>
