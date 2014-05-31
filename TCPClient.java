import java.io.*;
import java.net.*;


public class TCPClient {
	
	 static void clienteEnviaTCP(String mensaje,String flag) throws Exception
	{
		String sentence;
		String modifiedSentence;
		
		//BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));	
		InetAddress ip = InetAddress.getByName("192.168.0.4");
		Socket clientSocket = new Socket(ip,6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = mensaje.concat(":" + flag);
		outToServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		
		String[] sentenceArray = modifiedSentence.split(":");
		
		if(sentenceArray[1].equals("ACTUALIZAR"))
			actualizarHTML(sentenceArray[0]);
		System.out.println("FROM SERVER: " + modifiedSentence);
			
		clientSocket.close();
	}
	 
	 private static void actualizarHTML(String string) throws IOException {
			System.out.println("ACTUALIZAR HTML");
			System.out.println("STRING:"+ string);
					
			File f = new File("InterfazChat.html");
			
			//Se Parse el String y se extraen los datos
			
			String[] ArrayCadena = string.split("#");
			String ip_origen = ArrayCadena[0];
			String historial = ArrayCadena[1];	
			String[] ArrayHistorial = historial.split("\\$");
			int cantidad = ArrayHistorial.length - 1;
		

			if( f.exists())
			{;
	
				String cadena ="default";
				
				String[] cadenaArray;
					
				FileWriter w = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(w);
				PrintWriter wr = new PrintWriter(bw);
				
				wr.append("<html>\n"); 
				wr.append("<head>\n");
				wr.append("<title>Avioncito de Papel</title>\n");
				wr.append("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n");
				wr.append("<meta charset=\"UTF-8\">");
				wr.append("<style type=\"text/css\" media=\"screen\">\n");
				wr.append("body {\n");
				wr.append("margin: 10px;\n");
				wr.append(" }\n");
				wr.append(".form-control {\n");
				wr.append("width: 200px;\n");
				wr.append("}\n");
				wr.append("</style>\n");
				wr.append("</head>\n");
				wr.append("<body>\n");
				wr.append("<h2>Chat privado Avioncito de Papel </h2>\n");
				wr.append("<form class=\"form-horizontal\" action =\"InterfazChat.html\" method=\"post\">\n");
				wr.append("<!-- Nombre del form -->\n");
				wr.append("<strong>Nombre:</strong>\n");
				wr.append("<input name=\"message\" type=\"text\" style=\"width: 900px\" placeholder=\"Mensaje\" class=\"form-control\"><br>\n");
				wr.append("<strong>Enviar</strong><br>\n");
				wr.append("<button id=\"singlebutton\" type=\"submit\" value=\"Submit\" class=\"btn btn-primary\">SUBIR!</button>\n");
				wr.append("</form>\n");
			    
				for(int x = 0; x <= cantidad - 1; x++)
				{
					String[] ArrayMensaje = ArrayHistorial[x].split("!");
					String ip = ArrayMensaje[0];
					
					if(ip_origen.equals(ip))
							wr.append("<h3>"+ ArrayMensaje[1] +"</h3>\n");				
				}	
				wr.append("</body>");
				wr.append("</html>");
				wr.close();
						
			}	
			
		}

}
