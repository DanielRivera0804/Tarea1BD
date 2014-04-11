import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


class peticionWeb extends Thread
{
	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;

	void depura(String mensaje)
	{
		depura(mensaje,DEBUG);
	}

	void depura(String mensaje, int gravedad)
	{
		System.out.println(currentThread().toString() + " - " + mensaje);
	}

	private Socket scliente 	= null;		// Representa la petición de nuestro cliente
   	private PrintWriter out 	= null;		// Representa el buffer donde escribimos la respuesta

   	peticionWeb(Socket ps)
   	{
		scliente = ps;
		setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja
   	}

	public void run() // emplementamos el metodo run
	{
		System.out.println("[Procesamos la Conexion]");
		
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(scliente.getInputStream()));
			out = new PrintWriter (new OutputStreamWriter(scliente.getOutputStream(),"8859_1"),true) ;
			
			String cadena = "default";
			int i = 0;
			
			
			while(cadena != null && cadena.length() != 0)
			{
				//SE LEE UNA LINEA DE CADENA
				cadena = in.readLine();
				System.out.println("-- " + cadena);
				
				if(i == 0)
				{
					i++;
					
					StringTokenizer st = new StringTokenizer(cadena);
					
					if( st.countTokens() >= 2 )
					{
						String method = st.nextToken();
						
						if( method.equals("GET"))
						{
							retornaFichero(st.nextToken());
							
						}
						else if(method.equals("POST"))
						{
							procesarPost(in,st.nextToken());
							break;
						}
						else
						{
							System.out.println("[400 Peticion Incorrecta]");
						}

					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error en servidor\n" + e.toString());
		}
	}

	//Funcion que nos otorga recuperar el
	//Archivo solicitado por GET
	void retornaFichero(String sfichero)
	{

		// comprobamos si tiene una barra al principio
		if(sfichero.equals("/favicon.ico") )
			return;
		
		depura("Recuperamos el fichero " + sfichero);
		
		if (sfichero.startsWith("/"))
		{
			sfichero = sfichero.substring(1) ;
		}

	    // si acaba en /, le retornamos el index.htm de ese directorio
	    // si la cadena esta vacia, no retorna el index.htm principal
	    if (sfichero.endsWith("/") || sfichero.equals(""))
	    {
	    	sfichero = sfichero + "index.htm" ;
	    }

	    try
	    {

		    // Ahora leemos el fichero y lo retornamos
		    File mifichero = new File(sfichero) ;

		    if (mifichero.exists())
		    {
	  			out.println("HTTP/1.0 200 ok");
				out.println("Avioncito de Papel/1.0");
				out.println("Date: " + new Date());
				out.println("Content-Type: text/html");
				out.println("Content-Length: " + mifichero.length());
				out.println("\n");

				BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));

				String linea = "";

				do
				{
					linea = ficheroLocal.readLine();

					if (linea != null )
					{
						// sleep(500);
						out.println(linea);
					}
				}
				while (linea != null);

				depura("fin envio fichero");

				ficheroLocal.close();
				out.close();

			}  // fin de si el fichero existe
			else
			{
				depura("No encuentro el fichero " + mifichero.toString());
			}
		}
		catch(Exception e)
		{
			depura("Error al retornar fichero");
		}
	}
	
	void procesarPost(BufferedReader in,String sfichero) throws IOException
	{
		String cadena = "default";
		int length = 0; //Aca se guarda el largo de caracteres que contiene el POST
		while( cadena.equals("") == false)
		{
			cadena = in.readLine();
			
			//Se extrae el largo del POST
			if(cadena.startsWith("Content-Length"))
			{
				length = Integer.parseInt(cadena.substring(16));
			}
			System.out.println("#"+cadena);
		}
		
		//-------------------------------------------------
		// Se Llega hasta la linea NULL, 
		//...ahora se deben rescatar los datos ingresados
		//-------------------------------------------------
		
		int j = length;
		
		//Creamos el String 'query' con el arreglo nombre/valor con los chars correspondientes
		String  query= "";
		while( j != 0 ){

			query = query.concat(Character.toString((char)in.read()));
			j--;
		}
	
		guardarContacto(query);// El query se guarda en Contactos.txt con la funcion guardarcontacto
		retornaFichero(sfichero); // Se retorna a la pagina 
	}
	
	
	
	//Esta funcion guarda los datos en Contactos.txt
	//Cada linea representa un usuario
	void guardarContacto(String query) throws IOException
	{
		File f = new File("Contactos.txt");
		
		if(f.exists()) //Si el archivo con los contactos existen
		{
			FileWriter w = new FileWriter(f,true);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw); 

			String[] s = query.split("&");
			String usuario = s[0].substring(s[0].indexOf("=") + 1,s[0].length());
			String ip = s[1].substring(s[1].indexOf("=") + 1,s[1].length());
			String puerto = s[2].substring(s[2].indexOf("=") + 1,s[2].length());
			
			wr.append(usuario + "," + ip + ","+ puerto + "\n");
			wr.close();
			bw.close();
			
		}
		
	}
}

