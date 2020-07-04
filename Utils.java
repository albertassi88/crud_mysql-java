package a_mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Utils {
	
	static Scanner sc = new Scanner(System.in);
	
	public static Connection conectar() {
		String classe_driver = "com.mysql.jdbc.Driver";
		String usuario = "albertassi88";
		String senha = "#R@dio88FM";
		String url_servidor = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";
		
		try {
			Class.forName(classe_driver);
			return DriverManager.getConnection(url_servidor, usuario, senha);
		}catch (Exception e) {
			if(e instanceof ClassNotFoundException) {
				System.out.println("Verifique o driver de conexão");
			}else {
				System.out.println("Verifique se o servidor esta ativo");
			}System.exit(-42);
			return null;
		}
	}
	
	public static void desconectar(Connection conn) {
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Não foi possível fechar a conexão");
				e.printStackTrace();
			}
		}
	}
	
	public static void listar() {
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS);
			ResultSet res = produtos.executeQuery();
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();		
			
			if(qtd > 0) {
				System.out.println("Listando produtos....");				
				while(res.next()) {
					System.out.println("ID: "+ res.getInt(1));
					System.out.println("Produto: " + res.getString(2));
					System.out.println("Preço: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
				}
			}else{
				System.out.println("Não existem produtos cadastrados");
			}
			produtos.close();
			desconectar(conn);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro buscando produtos");
			System.exit(-42);
		}
	}
	
	public static void inserir() {
		System.out.println("Informe o nome do produto");
		String nome = sc.nextLine();
		
		System.out.println("Informe o preço do produto");
		float preco = sc.nextFloat();
		
		System.out.println("Informe a quantidade em estoque");
		int estoque = sc.nextInt();
		
		String inserir = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
		
		try {
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(inserir);
			
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);		
			salvar.executeUpdate();
			salvar.close();
			desconectar(conn);
			System.out.println("O produto " + nome + " foi inserido com sucesso.");			
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro salvando produtos");
			System.exit(-42);
		}
	}
	
	public static void atualizar() {
		System.out.println("Informe o código do produto");
		int id = Integer.parseInt(sc.nextLine());
		
		String buscar_por_id = "SELECT * FROM produtos WHERE id=?";
		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(buscar_por_id);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();			
			if(qtd > 0) {
				System.out.println("Informe o nome do produto");
				String nome = sc.nextLine();
				
				System.out.println("Informe o preço do produto");
				float preco = sc.nextFloat();
				
				System.out.println("Informe a quantidade em estoque");
				int estoque = sc.nextInt();
				
				String atualizar = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";
				PreparedStatement upd = conn.prepareStatement(atualizar);
				
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);				
				upd.executeUpdate();
				upd.close();
				desconectar(conn);
				System.out.println("O produto " + nome + " foi atualizado com sucesso");	
			}else {
				System.out.println("Não existe produto com o id informado");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar o produto");
			System.exit(-42);
		}
	}
	
	public static void deletar() {
		String deletar = "DELETE FROM produtos WHERE id=?";
		String buscar_por_id = "SELECT * FROM produtos WHERE id=?";		
		System.out.println("Informe o id do produto");
		int id = Integer.parseInt(sc.nextLine());		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(buscar_por_id);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();			
			if(qtd > 0) {
				PreparedStatement del = conn.prepareStatement(deletar);
				del.setInt(1, id);
				del.executeUpdate();
				del.close();
				desconectar(conn);
				System.out.println("O produto foi deletado com sucesso");
			}else {
				System.out.println("Não existe o produto com o id informado");
			}
			}catch (Exception e) {
				e.printStackTrace();
				System.err.println("Erro ao deletar o produto");
				System.exit(-42);
			}	
	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(sc.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida!");
		}
	}
}
