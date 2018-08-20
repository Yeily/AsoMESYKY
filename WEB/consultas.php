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
	} else if($_SERVER['REQUEST_METHOD']=='POST') {
	    try{
			if(isset($_POST['c'])){
			    $respuesta = FALSE;
			    $error = "El socio no existe";
			    
			    switch($_POST['c']){
			        case 4:
                        if(isset($_POST['socio'])){
                            $identificador = $_POST['socio'];
                            $nombre = $_POST['nombre'];
                            $fecha = $_POST['fecha'];
                            $telefono = $_POST['telefono'];
                            $correo = $_POST['correo'];
                            
                            if(!empty($fecha) and !empty($nombre)) {
		                        $respuesta = Datos::setSocio($identificador, $nombre, $fecha, $telefono, $correo);
                            }else{
                                $error = "Faltan datos";
                            }
                        }else{
		                    $error = "Falta el identificador";
		                }
                        break;
                    case 5:
                        if(isset($_POST['socio'])){
                            $identificador = $_POST['socio'];
                            $fecha = $_POST['fecha'];
                            $monto = $_POST['monto'];
                            $periodo = $_POST['periodo'];
                            $año = $_POST['año'];
                            
                            if(!empty($fecha) and !empty($monto) and !empty($año)){
		                        $respuesta = Datos::setAporte($identificador, $fecha, $monto, $periodo, $año);
                            }else{
                                $error = "Faltan datos";
                            }
                        }else{
		                    $error = "Falta el identificador";
		                }
                        break;
                    case 6:
                        if(isset($_POST['socio'])){
                            $identificador = $_POST['socio'];
                            $pass = $_POST['pass'];
                            
                            if(!empty($pass)){
		                        $respuesta = Datos::setPassword($identificador, $pass);
                            }else{
                                $error = "No estableció la contraseña";
                            }
                        }else{
		                    $error = "Falta el identificador";
		                }
                        break;
                    case 7:
                        $doc = $_POST['doc'];
                        $comp = $_POST['comp'];
                        $enti = $_POST['enti'];
                        $plan = $_POST['plan'];
                        $ini = $_POST['ini'];
                        $ven = $_POST['ven'];
                        $mon = $_POST['mon'];
                        $int = $_POST['int'];
                        $imp = $_POST['imp'];
                        $gan = $_POST['gan'];
                        $peri = $_POST['peri'];
                        $año = $_POST['año'];
                        
                        if(!empty($doc) and !empty($enti) and !empty($mon)){
	                        $respuesta = Datos::setInversion($doc, $comp, $enti, $plan, $ini, $ven, $mon, $int, $imp, $gan, $peri, $año);
                        }else{
                            $error = "Faltan datos";
                        }
                        break;
			    }
				
				$contenedor = array();
				
				if($respuesta){
					$contenedor["resultado"] = "OK";
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
	} else {
	    echo json_encode(array('resultado' => 'No se recibió una orden.'));
	}
?>