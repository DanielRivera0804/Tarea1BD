import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorHTTP
{
		int puerto = 90;
	
		public static void main(String[] array)
		{
				ServidorHTTP instancia = new ServidorHTTP(array);
				instancia.arranca();
		}
	
		ServidorHTTP(String[] param)
		{
				procesaParametros();
		}
	
		boolean procesaParametros()
		{		
				return true;
		}
	
		boolean arranca(){
		
				System.out.println("[Arrancamos nuestro servidor]");
		
				try
				{
					ServerSocket s = new ServerSocket(90);
					System.out.println("[Quedamos a la espera de conexion]");					
				
				while(true)// Bucle Infinito
				{ 
						Socket entrante = s.accept();		
						peticionWeb pCliente = new peticionWeb(entrante);
						pCliente.start();
				}				
		
				}
				catch(Exception e)
				{
						System.out.println("Error en servidor\n" + e.toString());
				}
				return true;
			}	
		}
	
	
	
	
