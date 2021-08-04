<?php
if (isset($_GET['room'])){ 
    $room = htmlentities($_GET['room'], ENT_QUOTES);
    
    include "connectDB.php";
    
    $sql="SELECT id FROM Placement WHERE number='$room'";
    $search=mysqli_query($link, $sql);
    if(mysqli_num_rows($search)==0){
        echo "6";
        return;
    }
    $todb=mysqli_fetch_array($search)[0];
    $sql="SELECT id FROM AssetPlacement WHERE id_placement=$todb";
    $search=mysqli_query($link, $sql);
    if(mysqli_num_rows($search)==0){
        echo "7";
        return;
    }
    
    $sql = "SELECT 	Asset.`id` AS idInventory,
                    Asset.`name` AS nameInventory,
                    Asset.`state` AS state
            FROM AssetPlacement 
    			JOIN Asset
                	ON Asset.id = AssetPlacement.id_asset
                JOIN Placement
                	ON AssetPlacement.id_placement = Placement.id 
                WHERE Placement.number=('$room') AND (state=1 OR state=2)";
   
    $rooms = mysqli_query($link,$sql);

    $mass = array();

    while($rws = mysqli_fetch_assoc($rooms)){
        $mass[$rws['idInventory']] = ($rws['nameInventory']);
    }
    $json_room_str = json_encode($mass, JSON_FORCE_OBJECT);
    echo $json_room_str;
    }
else
    echo "1";
?>