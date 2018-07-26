<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
	
	require_once('./adminBD/confingBD.php');
	
	class ConexionBD(){
		private static $cxn = null;
		private static $pdo;
		
		public function __construct() {
			try {
				self::getInstancia();
				self::conectar();
			} catch(PDOException $ex) {
				echo $ex;
			}
		}
		
		protected function __clone() { }
		
		private function getInstancia() {
			if(self::$cxn === null) {
				self::$cxn = new self();
			}
			
			return self::$cxn; 
		}
		
		protected function conectar() {
			if(self::$pdo == null) {
				self::$pdo = new PDO(
					'mysql:dbname='.BD_NOMBRE.';host='.BD_SERVIDOR.';port:63343;',
					BD_USUARIO,
					BD_PASSWORD,
					array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
				);
				
				self::$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			}
			
			return self::$pdo;
		}
		
		public function __destruct() {
			if(self::$pdo <> null) {
				self::$pdo->close();
				self::$pdo = null;
			}
			
			if(self::$cxn <> null) {
				self::$cxn->close();
				self::$cxn = null;
			}
		}
	}
?>