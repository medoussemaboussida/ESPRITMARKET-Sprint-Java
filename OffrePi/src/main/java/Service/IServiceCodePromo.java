package Service;

import entities.CodePromo;

import java.util.List;

public interface IServiceCodePromo <C>{
    void addCodePromo(CodePromo c);

    List<CodePromo> readCodePromo();

    void deleteCodePromo(int id);


    void modifyCodePromo(CodePromo c);
}
