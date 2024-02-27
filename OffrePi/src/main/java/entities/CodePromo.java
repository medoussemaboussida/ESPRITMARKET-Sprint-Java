package entities;

public class CodePromo {
    private int idCode;
    private int reductionAssocie;
    private String code;
    private boolean utilise;
    private String email;

    public CodePromo(int idCode, int reductionAssocie, String code, boolean utilise, String email) {
        this.idCode = idCode;
        this.reductionAssocie = reductionAssocie;
        this.code = code;
        this.utilise = utilise;
        this.email = email;
    }

    public CodePromo(String email) {
    }

    public int getIdCode() {
        return idCode;
    }

    public void setIdCode(int idCode) {
        this.idCode = idCode;
    }

    public int getReductionAssocie() {
        return reductionAssocie;
    }

    public void setReductionAssocie(int reductionAssocie) {
        this.reductionAssocie = reductionAssocie;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUtilise() {
        return utilise;
    }

    public void setUtilise(boolean utilise) {
        this.utilise = utilise;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CodePromo{" +
                "idCode=" + idCode +
                ", reductionAssocie=" + reductionAssocie +
                ", code='" + code + '\'' +
                ", utilise=" + utilise +
                ", email='" + email + '\'' +
                '}';
    }

    // Méthode pour envoyer un e-mail contenant le code promo
    public void sendPromoCodeEmail() {
        // Code pour envoyer un e-mail à l'utilisateur avec le code promo
        String subject = "Votre code promo pour votre prochain achat";
        String message = "Bonjour,\n\n"
                + "Merci d'avoir fait un don à notre épicerie en ligne. Voici votre code promo pour votre prochain achat : "
                + code + ". Utilisez ce code lors de votre prochaine commande pour bénéficier de la réduction.\n\n"
                + "Cordialement,\n"
                + "L'équipe de l'épicerie en ligne";

        // Envoi de l'e-mail à l'utilisateur
        // Ici, vous devez implémenter la logique pour envoyer un e-mail à l'utilisateur avec les informations nécessaires
        // Par exemple, vous pouvez utiliser JavaMail API ou une bibliothèque similaire pour envoyer des e-mails.
        // Voici un exemple simple pour l'illustration uniquement :
        System.out.println("Envoi de l'e-mail à : " + email);
        System.out.println("Sujet : " + subject);
        System.out.println("Message : " + message);
        System.out.println("E-mail envoyé avec succès !");
    }

    // Méthode pour vérifier si le code promo peut être utilisé
    public boolean peutUtiliser() {
        return !utilise;
    }

    // Méthode pour marquer le code promo comme utilisé
    public void utiliser() {
        this.utilise = true;
    }

    /*// Méthode principale pour tester
    public static void main(String[] args) {
        // Exemple d'utilisation de la classe CodePromo
        String email = "exemple@example.com";
        CodePromo codePromo = new CodePromo(email);
        codePromo.sendPromoCodeEmail();

        // Supposons que le code promo a été utilisé lors de l'achat
        codePromo.utiliser();

        // Vous pouvez vérifier si le code promo peut être utilisé avant de l'appliquer lors de l'achat
        if (codePromo.peutUtiliser()) {
            // Appliquer le code promo lors de l'achat
            System.out.println("Le code promo peut être utilisé.");
        } else {
            System.out.println("Le code promo a déjà été utilisé.");
        }
    }*/
}