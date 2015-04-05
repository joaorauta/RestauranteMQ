/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Funcionario;
import model.Item;
import model.ItemPronto;

public class ItemProntoDAO implements Dao<ItemPronto, Long>{

    @Override
    public void save(ItemPronto entity) {
        ConexaoPostgreSQL conn = null;
        try {
            conn = new ConexaoPostgreSQL("localhost", "postgres", "postgres", "postgres");

            String sql = "insert into itemdemenu (nome, preco, categoria, quantidadeestoque)  "
                    + "values(?,?,?,?)";

            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
                ps.setString(1, entity.getNome());
                ps.setDouble(2, entity.getPreco());
                ps.setString(3, entity.getCategoria());
                ps.setInt(4, entity.getQuantidadeEstoque());
                
               
                ps.execute();

                conn.fechar();
            }

        } catch (Exception ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            if (conn != null) {
                conn.fechar();
            }
        }
    }

    @Override
    public void delete(Long id) {
        ConexaoPostgreSQL conn = null;
        try {
            conn = new ConexaoPostgreSQL("localhost", "postgres", "postgres", "postgres");

            String sql = "delete from itemdemenu where id = ?";

            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.execute();

                conn.fechar();
            }

        } catch (Exception ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (conn != null) {
                conn.fechar();
            }
        }
    }

    @Override
    public List<ItemPronto> listAll() {
        List<ItemPronto> lista = new ArrayList<>();

        ConexaoPostgreSQL conn = null;
        try {
            conn = new ConexaoPostgreSQL("localhost", "postgres", "postgres", "postgres");

            String sql = "select * from itemdemenu";

            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ItemPronto c = new ItemPronto();
                    c.setId(rs.getLong("id"));
                    c.setNome(rs.getString("nome"));
                    c.setPreco(rs.getDouble("preco"));
                    c.setCategoria(rs.getString("categoria"));
                    c.setQuantidadeEstoque(rs.getInt("quantidadedeestoque"));
                    lista.add(c);

                }

            }
            conn.fechar();

        } catch (Exception ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (conn != null) {
                conn.fechar();
            }
        }

        return lista;
    }

    @Override
    public ItemPronto getById(Long pk) {
        ItemPronto c = new ItemPronto();
        ConexaoPostgreSQL conn = null;
        try {
            conn = new ConexaoPostgreSQL("localhost", "postgres", "postgres", "postgres");

            String sql = "select * from itemdemenu where id = ?";

            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {

                ps.setLong(1, pk);

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    c.setId(rs.getLong("id"));
                    c.setNome(rs.getString("nome"));
                    c.setPreco(rs.getDouble("preco"));
                    c.setCategoria(rs.getString("categoria"));
                    c.setQuantidadeEstoque(rs.getInt("quantidadedeestoque"));
                    
                } else {
                    throw new Exception("Não há item com o id " + pk);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                conn.fechar();
            }
        }

        return c;
    }
    
}