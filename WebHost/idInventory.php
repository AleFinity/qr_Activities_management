<?php
if (isset($_GET['number'])){ 
    $number = htmlentities($_GET['number']);
    
    include "connectDB.php";
    
    if(isset($_GET['login'])){
        $login = htmlentities($_GET['login'], ENT_QUOTES);
        $sql1=" SELECT id_kafedra FROM `Employee` WHERE login='$login'";
        $sql2=" SELECT AssetKafedra.`id_kafedra` FROM AssetKafedra WHERE AssetKafedra.id_asset=($number)";
        $result1=mysqli_fetch_array(mysqli_query($link, $sql1))[0];
        $result2=mysqli_fetch_array(mysqli_query($link, $sql2))[0];
        
        if($result1!=$result2){
            echo "2";
            return;}
    }
    
    if(!isset($_GET['login']) || $result1==$result2){
        $idInv = (int)$number;
        $ret=mysqli_query($link,"SELECT id FROM Asset WHERE id = $idInv LIMIT 1") or die("1");

        if(mysqli_fetch_array($ret)[0]!=null){
            $sql="SELECT 1 FROM AssetKafedra WHERE id_asset=$number";
            $search=mysqli_query($link, $sql);
            if(mysqli_num_rows($search)>0)
                $sql = "
                SELECT 		`Asset`.`id` AS idInventory,
                        `Asset`.`name` AS nameInventory,
                        `TypeAsset`.typeAsset AS typeInventory, 
                        `Asset`.`assetNumber`AS assetNumber, 
                        `Asset`.`factoryNumber` AS factoryNumber,
                        `Asset`.`dateOfAcceptance` AS dateOfAcceptance, 
                        `Asset`.`cost` AS cost, 
                        `Asset`.`characteristic` AS characteristic,
                        Kafedra.`simpleName` AS simpleNameKafedra, 
                        Kafedra.name AS nameKafedra,
                        Placement.number AS numberClass, 
                        TypesClasses.typeClass AS typeClass,
                        `Asset`.`state` AS state,
                        `Repair`.`characteristicBreak` AS problem,
                        `Repair`.`dateRepair` AS dateRepair
                FROM `AssetPlacement` 
        			JOIN `Asset`
                    	ON `Asset`.id = `AssetPlacement`.id_asset
                    JOIN `TypeAsset`
                    	ON `Asset`.id_typeAsset = TypeAsset.id
                    LEFT JOIN Placement
                    	ON `AssetPlacement`.id_placement = Placement.id 
                    JOIN `AssetKafedra`
                    	ON AssetKafedra.id_asset = `Asset`.id
                    LEFT JOIN Kafedra
                    	ON Kafedra.id = `AssetKafedra`.id_kafedra
                    LEFT JOIN TypesClasses
                    	ON TypesClasses.idTypeClasses = Placement.id_typeClass
                    LEFT JOIN `AssetRepair`
                    	ON `Asset`.id = `AssetRepair`.idAsset
                    LEFT JOIN `Repair`
                    	ON `AssetRepair`.idRepair=`Repair`.id
                    WHERE `Asset`.id=($idInv) ORDER BY `Repair`.id DESC LIMIT 1";   
            else
                $sql = "
                    SELECT 	`Asset`.`id` AS idInventory,
                            `Asset`.`name` AS nameInventory,
                            `TypeAsset`.typeAsset AS typeInventory, 
                            `Asset`.`assetNumber`AS assetNumber, 
                            `Asset`.`factoryNumber` AS factoryNumber,
                            `Asset`.`dateOfAcceptance` AS dateOfAcceptance, 
                            `Asset`.`cost` AS cost, 
                            `Asset`.`characteristic` AS characteristic,
                            NULL AS simpleNameKafedra, 
                            NULL AS nameKafedra,
                            NULL AS numberClass, 
                            NULL AS typeClass,
                            `Asset`.`state` AS state,
                            NULL AS problem,
                            NULL AS dateRepair
                    FROM `Asset`
                        JOIN `TypeAsset`
                        	ON `Asset`.id_typeAsset = TypeAsset.id
                        WHERE `Asset`.id=($idInv)";
            
                $inventory=mysqli_query($link,$sql);
                $inventoryInfo=mysqli_fetch_array($inventory);
                if($inventoryInfo[14]!=NULL)
                    $inventoryInfo['problem']=NULL;
                $json_inv_str = json_encode($inventoryInfo);
                echo $json_inv_str;
        }
        else
            echo "3";
    }else 
    	echo "2";
}
else
    echo "2";
?>