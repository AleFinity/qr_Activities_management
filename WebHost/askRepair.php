<?php
if (isset($_GET['number']) && isset($_GET['problem'])) {
    $number = htmlentities($_GET['number']);
    $prob = htmlentities($_GET['problem'], ENT_QUOTES);
    
    include "connectDB.php";
    
    $sql="  SELECT characteristicBreak FROM Repair 
            WHERE id=(SELECT `idRepair` FROM `AssetRepair` WHERE `idAsset`=$number ORDER BY id DESC LIMIT 1) 
            AND dateRepair IS NULL";
    $result=mysqli_query($link, $sql);
    
    if(mysqli_num_rows($result)>0){
        $field=mysqli_fetch_array($result)[0];
        $sumProblems = $field ."\n". $prob;
        $sql1=" UPDATE `Repair` SET `characteristicBreak`='$sumProblems'
                WHERE id=(SELECT `idRepair` FROM `AssetRepair` WHERE `idAsset`=$number ORDER BY id DESC LIMIT 1)";
        $result1=mysqli_query($link, $sql1);
        if (!$result1) {
            echo "1";
        }
        else
            echo "14";
        }
    else{
         $sql1="
        INSERT INTO `Repair`(`dateBreak`, `characteristicBreak`) VALUES (CURRENT_DATE(), '$prob')";
    $sql2="INSERT INTO `AssetRepair`(`idAsset`, `idRepair`) VALUES ($number, (SELECT `id` FROM `Repair` ORDER BY `id` DESC LIMIT 1))";
    
    $result1=mysqli_query($link, $sql1);
    $result2=mysqli_query($link, $sql2);
    
    if (!$result1 || !$result2) {
        echo "1";
    }
    else
        echo "14";
    }
}
else
    echo "1";
?>