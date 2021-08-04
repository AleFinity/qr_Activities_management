<?php
if (isset($_GET['number']) && isset($_GET['login'])){ 
    $number = htmlentities($_GET['number']);
    $login = htmlentities($_GET['login'], ENT_QUOTES);
    
    include "connectDB.php";
    
    $sql="SELECT state FROM `Asset` WHERE `id`=$number";
    $state=mysqli_fetch_array(mysqli_query($link, $sql))[0];
    
    if($state==0) // МА принимается впервые
    {
        $sql1="SELECT id_kafedra FROM `Employee` WHERE login='$login'";
        $result1=mysqli_fetch_array(mysqli_query($link, $sql1))[0];
        $sql2="INSERT INTO `AssetKafedra`(`id_asset`, `id_kafedra`) VALUES ($number,$result1)";
        $sql3="INSERT INTO `AssetPlacement`(`id_asset`) VALUES ($number)";
        $sql4="UPDATE `Asset` SET `state`=1 WHERE `id`=$number";
        
        $result2=mysqli_query($link, $sql2);
        $result3=mysqli_query($link, $sql3);
        $result4=mysqli_query($link, $sql4);
        
        if (!$result2 || !$result3 || !$result4)
            echo "1";
        else
            echo "20";
        return;
    }
    /*
    $sql1=" SELECT id_kafedra FROM `Employee` WHERE login='$login'";
    $sql2=" SELECT AssetKafedra.`id_kafedra` FROM AssetKafedra WHERE AssetKafedra.id_asset=($number)";
    $result1=mysqli_fetch_array(mysqli_query($link, $sql1))[0];
    $result2=mysqli_fetch_array(mysqli_query($link, $sql2))[0];
    
    if($result1!=$result2){
        echo "2";
        return;
    }*/
    
    if($state==3) // МА находится в процессе передачи на другую кафедру
    {
        $sql1="UPDATE `TransferKafedra` SET `dateTaken`=CURRENT_DATE() WHERE `id_asset`=$number";
        $sql2="UPDATE `AssetKafedra` SET `id_kafedra`=(SELECT id_newKafedra FROM `TransferKafedra` WHERE `id_asset`=$number) WHERE `id_asset`=$number";
        $sql3="UPDATE `Asset` SET `state`=1 WHERE `id`=$number";
        
        $result1=mysqli_query($link, $sql1);
        $result2=mysqli_query($link, $sql2);
        $result3=mysqli_query($link, $sql3);
        if (!$result1 || !$result2 || !$result3)
            echo "1";
        else
            echo "20";
    }
    else if($state==4) // МА находится в пользовании сотрудника;
    {
        $sql1="UPDATE `GiveAssetTemporarily` SET `dateReturn`=CURRENT_DATE() WHERE `id_asset`=$number";
        $sql2="UPDATE `Asset` SET `state`=1 WHERE `id`=$number";
        
        $result1=mysqli_query($link, $sql1);
        $result2=mysqli_query($link, $sql2);
        if (!$result1 || !$result2)
            echo "1";
        else
            echo "21";
    }
}
else
    echo "1";
?>