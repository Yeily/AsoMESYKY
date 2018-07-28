<?php
	require './adminBD/API.php';
	
	if($_SERVER['REQUEST_METHOD']=='GET'){
		try{
			if(isset($_GET['c'])){
			    $respuesta = FALSE;
			    $error = "El socio no existe";
			    
			    switch($_GET['c']){
			        case 1:
			            $respuesta = Datos::getSocios();
			            break;
		            case 2:
		                if(isset($_GET['socio'])){
    		                $identificador = $_GET['socio'];
    		                $respuesta = Datos::getSocio($identificador);
		                }else{
		                    $error = "Falta el identificador";
		                }
		                break;
                    case 3:
                        if(isset($_GET['socio'])){
                            $identificador = $_GET['socio'];
		                    $respuesta = Datos::getSaldo($identificador);
                        }else{
		                    $error = "Falta el identificador";
		                }
                        break;
			    }
				
				$contenedor = array();
				
				if($respuesta){
					$contenedor["resultado"] = "OK";
					$contenedor["datos"] = $respuesta;
					echo json_encode($contenedor);
				}else{
					echo json_encode(array('resultado' => $error));
				}
			}else{
				echo json_encode(array('resultado' => 'Falta el identificador'));
			}
		}catch(PDOException $ex){
			echo $ex;
		}
	}
?>