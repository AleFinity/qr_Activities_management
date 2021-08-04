<?php
if (isset($_GET['login']) && isset($_GET['password'])){
    $login = htmlentities($_GET['login'], ENT_QUOTES);
    $passw = htmlentities($_GET['password'], ENT_QUOTES);

    include "connectDB.php";
    
    $sql="SELECT 1 FROM Employee WHERE login='$login'";
    $search=mysqli_query($link, $sql) or die("2");
    if(mysqli_num_rows($search)>0){
        $sql="SELECT id FROM `Employee` WHERE `login` = '$login' AND `password` = '$passw' AND `responsibleStatus` = 1 LIMIT 1";
        $result = mysqli_query($link, $sql) or die("1");
        if(mysqli_fetch_array($result)[0]!=null)
            echo "12";
        else
            echo "2";
    }
    else
        echo "2";
}
else
    echo "1";
?>
