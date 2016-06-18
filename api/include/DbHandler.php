<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `users` method ------------------ */

    /**
     * Creating new user
     * @param String $name User name
     * @param String $password User login password
     */
    public function createUser($user, $password) {
        require_once 'PassHash.php';
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($user)) {
            // Generating password hash
            $password_hash = PassHash::hash($password);

            // Generating API key
            $api_key = $this->generateApiKey();

            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(name, pass, api_key) values(?, ?, ?)");
            $stmt->bind_param("sss", $user, $password_hash, $api_key);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return USER_CREATED_SUCCESSFULLY;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            return USER_ALREADY_EXISTED;
        }

        return $response;
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($user, $password) {
        // fetching user
        $stmt = $this->conn->prepare("SELECT pass FROM users WHERE name = ?");

        $stmt->bind_param("s", $user);

        $stmt->execute();

        $stmt->bind_result($password_hash);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // Found user
            // Now verify the password

            $stmt->fetch();

            $stmt->close();

            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                return 0;
            } else {
                // user password is incorrect
                return 2;
            }
        } else {
            $stmt->close();

            // user not existed
            return 1;
        }
    }

    /**
     * Checking for duplicate user
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUserExists($user) {
        $stmt = $this->conn->prepare("SELECT name from users WHERE name = ?");
        $stmt->bind_param("s", $user);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }


    /**
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user) {
        $stmt = $this->conn->prepare("SELECT api_key FROM users WHERE name = ?");
        $stmt->bind_param("s", $user);
        if ($stmt->execute()) {
            $api_key = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user by api key
     * @param String $api_key user api key
     */
    public function getUserId($api_key) {
        $stmt = $this->conn->prepare("SELECT name FROM users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $api_key user api key
     * @return boolean
     */
    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT name from users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }



    /* ------------- `recipes` method ------------------ */
    /**
     * Creating new recipe
     */
    public function createRecipe($user, $name, $details, $picture, $difficulty, $time, $diners) {
        $stmt = $this->conn->prepare("INSERT INTO recipes( name, details, picture, difficulty, time, diners, creator) VALUES(?,?,?,?,?,?,?)");
        $stmt->bind_param("sssiiis", $name, $details, $picture, $difficulty,$time, $diners, $user);

        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            // task row created
            return $this->conn->insert_id;
        } else {
            // task failed to create
            return NULL;
        }
    }
    /**
     * Creating ingredients
     */
    public function createIngredient($name) {
        $stmt = $this->conn->prepare("INSERT INTO ingredients(name) values( ?)");
        $stmt->bind_param("s", $name);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            // task row created
            return $this->conn->insert_id;
        } else {
            // task failed to create
            return NULL;
        }
    }
    /**
     * Set recipe ingredients quantity
     */
    public function createQuantity($id_recipe, $id_ingredient, $cant, $measure) {
        $stmt = $this->conn->prepare("INSERT INTO quantity( idrecipe, idingredient, cant, measure) VALUES(?,?,?,?)");
        $stmt->bind_param("iiis", $id_recipe, $id_ingredient, $cant, $measure);

        $result = $stmt->execute();
        $stmt->close();
        return $result;

    }
    /**
     * Set recipe ingredients quantity
     */
    public function createStep($id_recipe, $step, $picture) {
        $stmt = $this->conn->prepare("INSERT INTO making( idrecipe,  step, picture) VALUES(?,?,?)");
        $stmt->bind_param("iss", $id_recipe,  $step, $picture);

        $result = $stmt->execute();
        $stmt->close();
        return $result;
    }

    public function createFav($user,$id_recipe) {
        $stmt = $this->conn->prepare("INSERT INTO favs(iduser, idrecipe) values( ?,?)");
        $stmt->bind_param("si", $user,$id_recipe);
        $result = $stmt->execute();
        $stmt->close();

        return $result;
    }




    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserRecipes($user) {
        $stmt = $this->conn->prepare("SELECT * FROM recipes WHERE creator = ?");
        $stmt->bind_param("s", $user);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }

    public function getUserFavAndCreatedRecipes($user){
      $stmt = $this->conn->prepare("SELECT * FROM recipes where creator=? or idrecipe IN(select idrecipe from favs where iduser=?)");
      $stmt->bind_param("ss", $user,$user);
      $stmt->execute();
      $recipes = $stmt->get_result();
      $stmt->close();
      return $recipes;

    }

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserFavs($user) {
        $stmt = $this->conn->prepare("SELECT r.* FROM recipes r, favs f WHERE r.idrecipe = f.idrecipe AND iduser = ?");
        $stmt->bind_param("s", $user);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }


    public function getAllRecipeQuantity($id_recipe) {
        $stmt = $this->conn->prepare("SELECT q.idingredient, i.name, q.cant, q.measure FROM ingredients i, quantity q WHERE i.idingredient = q.idingredient AND q.idrecipe = ?");
        $stmt->bind_param("i", $id_recipe);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }

    public function getAllRecipeStep($id_recipe) {
        $stmt = $this->conn->prepare("SELECT idmaking, step, picture FROM making WHERE idrecipe = ?");
        $stmt->bind_param("i", $id_recipe);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }

    public function getRecipeById($idrecipe) {
        $stmt = $this->conn->prepare("SELECT * FROM recipes WHERE idrecipe = ?");
        $stmt->bind_param("i", $idrecipe);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }

    public function getRecipesByName($name) {
        $stmt = $this->conn->prepare("SELECT * FROM recipes WHERE name LIKE ?");
        $stmt->bind_param("s", $name);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }
    public function deleteFav($user_id, $recipe_id) {
        $stmt = $this->conn->prepare("DELETE FROM favs WHERE iduser = ? AND idrecipe = ?");
        $stmt->bind_param("si", $user_id, $recipe_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    public function getAllRecipes(){
        $stmt = $this->conn->prepare("SELECT * FROM recipes");
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }
    public function getAllIngredients(){
        $stmt = $this->conn->prepare("SELECT * FROM ingredients");
        $stmt->execute();
        $ingredients = $stmt->get_result();
        $stmt->close();
        return $ingredients;
    }

    ///// Primer ? desconocido si funcionara
    public function getRecipesByIngredients($idIngredients, $numIngredients){
        $stmt = $this->conn->prepare("select * from recipes where idrecipe in (select idrecipe from quantity where idingredient in ? group by 1 having count(*)=?)");
        $stmt->bind_param("ii", $idIngredients, $numIngredients);
        $stmt->execute();
        $recipes = $stmt->get_result();
        $stmt->close();
        return $recipes;
    }





    public function checkFavs($idrecipe,$user) {
        $stmt = $this->conn->prepare("SELECT * FROM favs WHERE idrecipe = ? AND iduser = ?");
        $stmt->bind_param("is", $idrecipe, $user);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }


    public function returnIngredient($name){
      $stmt = $this->conn->prepare("SELECT idingredient FROM ingredients WHERE name=?");
      $stmt->bind_param("s", $name);
      $stmt->execute();
      $ingredient =$stmt->get_result()->fetch_assoc();
      if(is_null(ingredient['idingredient'])){
        $idingredient=createIngredient($name);
      }else{
        $idingredient=ingredient['idingredient'];
      }
      $stmt->close();
      return $idingredient;

    }







    ///////SIN Mirar
    /**                                                                                                             d
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**                                                                                                                     d
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deleteRecipe($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }



}

?>
