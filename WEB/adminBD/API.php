<?php
	require_once './adminBD/conexionBD.php';
	
	class Datos {
		public function __construct() { }
		
		//c = 1
		public function getSocios() {
			$consulta = "SELECT * FROM Socios";
			$parametros = array();
			
			return self::consultar($consulta, $parametros);
		}
		
		//c = 2
		public function getSocio($socio) {
		    $consulta = "SELECT * FROM Socios WHERE Socio = ?";
		    $parametros = array($socio);
		    
			return self::consultar($consulta, $parametros);
		}
		
		//c = 3
		public function getSaldo($socio) {
		    $consulta = "SELECT S.Nombre Nombre, SUM(A.Aporte) Saldo FROM Aportes A INNER JOIN Socios S ON S.Socio = A.Socio WHERE A.Socio = ? GROUP BY S.Nombre";
		    $parametros = array($socio);
		    
			return self::consultar($consulta, $parametros);
		}
		
		//c = 4
		public function setSocio($codigo, $nombre, $password, $fecha, $activo, $telefono, $correo){
		    $consulta = "INSERT INTO Socios (Socio, Nombre, Pass, FechaIngreso, Activo, Telefono, Correo) VALUES (?,?,?,?,?,?,?)";
		    $parametros = array($codigo, $nombre, $password, $fecha, $activo, $telefono, $correo);
		    
		    return self::ejecutar($consulta, $parametros);
		}
		
		protected function consultar($consultaSQL, $parametros) {
			try {
			    $result = array();
				$comando = ConexionBD::conectar()->prepare($consultaSQL);
				
				if(count($parametros) > 0) {
				    $comando->execute($parametros);
				} else {
				    $comando->execute();   
				}
				
				$i=0;
				while($fila = $comando->fetch(PDO::FETCH_ASSOC)){
				    $result[$i]=$fila;
				    $i++;
				}
				
				return $result;
			} catch(PDOException $ex) {
				return false;
			}
		}
		
		protected function ejecutar($consultaSQL, $parametros) {
			try {
				$comando = ConexionBD::conectar()->prepare($consultaSQL);
				
				if(count($parametros) > 0) {
				    $comando->execute($parametros);
				} else {
				    $comando->execute();   
				}
				
				return $comando;
			} catch(PDOException $ex) {
				return false;
			}
		}
	}
	
?>