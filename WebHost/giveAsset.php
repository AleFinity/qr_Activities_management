<?php
if (isset($_GET['number']) && isset($_GET['kafedraOld']) && isset($_GET['kafedraNew'])){ 
    $number = htmlentities($_GET['number']);
    $kafedraOld = htmlentities($_GET['kafedraOld']);
    $kafedraNew = htmlentities($_GET['kafedraNew']);

    include "connectDB.php";
    
    $sql="SELECT 1 FROM Kafedra WHERE name='$kafedraNew'";
    $search=mysqli_query($link, $sql);
    if(mysqli_num_rows($search)==0){
        echo "5";
        return;
    }
    
    $sql="SELECT state FROM Asset WHERE id=$number";
    $result=mysqli_fetch_array(mysqli_query($link, $sql))[0];
    if($result==3){
         $sql1="
            UPDATE `TransferKafedra` SET `id_newKafedra`=(SELECT id FROM Kafedra WHERE name='$kafedraNew')";
    }
    else{
        $sql1="
            INSERT INTO `TransferKafedra`(`id_asset`, `id_oldKafedra`, `id_newKafedra`, `dateGiven`) VALUES (
            $number,(SELECT id FROM Kafedra WHERE name='$kafedraOld'),(SELECT id FROM Kafedra WHERE name='$kafedraNew'),CURRENT_DATE())";
    }
    $sql2="UPDATE `Asset` SET `state`=3 WHERE `id`=$number";
    
    $result1=mysqli_query($link, $sql1);
    $result2=mysqli_query($link, $sql2);
    
    if (!$result1 || !$result2) {
        echo "1";
    }
    else
        echo "15";
}
else
    echo "1";
?>