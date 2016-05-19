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
    if ($db->checkLogin($user, $password)) {
        // get the user by email
        $user = $db->getApiKeyById($user);

        if ($user != NULL) {
            $response["error"] = false;
            $response['apiKey'] = $user['api_key'];
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
    $response["recipes"] = array();

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
        array_push($response["recipes"], $tmp);
    }

    echoRespnse(200, $response);
});
/**
 * Listing single task of particual user
 * method GET
 * url /tasks/:id
 * Will return 404 if the task doesn't belongs to user
 */


//$app->post('/recipes', 'authenticate', function() use ($app) {
//    // check for required params
//    verifyRequiredParams(array('recipe'));
//
//    $response = array();
//    $recipe = $app->request->post('recipe');
//
//
//    global $user_id;
//    $db = new DbHandler();
//
//    // creating new task
//    $recipe_id = $db->createRecipe($user_id, $name, $details, $picture, $difficulty, $time, $diners);
//
//    if ($recipe_id != NULL) {
//        $response["error"] = false;
//        $response["message"] = "Task created successfully";
//        $response["recipe_id"] = $recipe_id;
//    } else {
//        $response["error"] = true;
//        $response["message"] = "Failed to create task. Please try again";
//    }
//    echoRespnse(201, $response);
//});


//$app->get('/recipes/:id', 'authenticate', function($recipe_id) use($app) {
//
//    $response = array();
//    $db = new DbHandler();
//
//    // fetch task
//    $result = $db->getRecipeById($recipe_id);
//
//    if ($result != NULL) {
//        $response["error"] = false;
//        $response["idrecipe"] = $result["idrecipe"];
//        $response["name"] = $result["name"];
//        $response["details"] = $result["details"];
//        $response["picture"] = $result["picture"];
//        $response["difficulty"] = $result["difficulty"];
//        $response["time"] = $result["time"];
//        $response["diners"] = $result["diners"];
//        $response["creator"] = $result["creator"];
//        echoRespnse(200, $response);
//    } else {
//        $response["error"] = true;
//        $response["message"] = "The requested resource doesn't exists";
//        echoRespnse(404, $response);
//    }
//});












$app->run();























?>