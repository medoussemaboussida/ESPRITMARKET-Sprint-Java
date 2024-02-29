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
        String insertCodePromoQuery = "insert into codePromo ( code,reductionAssocie,dateDebut,dateFin) values (?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(insertCodePromoQuery, Statement.RETURN_GENERATED_KEYS);;
            pst.setInt(2,c.getReductionAssocie());
            pst.setString(1,c.getCode());
            if (c.getDateDebut() != null) {
                pst.setDate(3, new java.sql.Date(c.getDateDebut().getTime()));
            } else {
                pst.setNull(3, Types.DATE);
            }
            // Gestion de la date de fin
            if (c.getDateFin() != null) {
                pst.setDate(4, new java.sql.Date(c.getDateFin().getTime()));
            } else {
                pst.setNull(4, Types.DATE);
            }

            pst.executeUpdate();
            System.out.println("Code promo ajouté!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<CodePromo> readCodePromo() throws SQLException {
        String requete = "select * from codePromo";
        List<CodePromo> list=new ArrayList<>();
        try {
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(requete);
            while (rs.next()) {
                CodePromo codePromo = new CodePromo();
                codePromo.setIdCode(rs.getInt("idCode"));
                codePromo.setReductionAssocie(rs.getInt("reductionAssocie"));
                codePromo.setCode(rs.getString("code"));
                codePromo.setDateDebut(rs.getDate("dateDebut"));
                codePromo.setDateFin(rs.getDate("dateFin"));
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
        String requete = "UPDATE CodePromo set Code = ?, reductionAssocie = ?, dateDebut = ?, dateFin = ? where idCode= ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1,c.getCode() );
            pst.setInt(2,c.getReductionAssocie());
            pst.setDate(3, new java.sql.Date(c.getDateDebut().getTime()));
            pst.setDate(4, new java.sql.Date(c.getDateFin().getTime()));
            pst.setInt(5,c.getIdCode());

            pst.executeUpdate();
            System.out.println("Code promo Modifiée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



}
