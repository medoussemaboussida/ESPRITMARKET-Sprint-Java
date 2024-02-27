package Service;

import entities.CodePromo;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CodePromoService implements IServiceCodePromo <CodePromo>  {

    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public CodePromoService()
    {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void addCodePromo(CodePromo c) {
        String requete = "insert into CodePromo (idCode ,reductionAssocie , code,utilise,email) values (?,?,?,?,?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1,c.getIdCode() );
            pst.setInt(2,c.getReductionAssocie());
            pst.setString(3,c.getCode());
            pst.setBoolean(4,c.isUtilise());
            pst.setString(5,c.getEmail());

            pst.executeUpdate();
            System.out.println("Code promo ajouté!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<CodePromo> readCodePromo()
    {
        String requete = "select * from CodePromo";
        List<CodePromo> list=new ArrayList<>();
        try {
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(requete);
            while (rs.next()) {
                CodePromo codePromo = new CodePromo(rs.getInt(1), rs.getInt(2), rs.getString(3),rs.getBoolean(4),rs.getString(5));
                list.add(codePromo);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void deleteCodePromo(int id) {
        String requete = "delete from CodePromo where idCode = ?";
        try {
            pst=conn.prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("Code Promo supprimé!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void modifyCodePromo(CodePromo c)
    {
        String requete = "UPDATE CodePromo set nomCode = ?, reductionAssocie = ? where idCode= ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1,c.getCode() );
            pst.setInt(2,c.getReductionAssocie());
            pst.setInt(3,c.getIdCode());
            pst.executeUpdate();
            System.out.println("Code promo Modifiée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



}
