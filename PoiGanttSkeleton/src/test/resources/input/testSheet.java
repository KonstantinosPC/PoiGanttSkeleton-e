import java.service.IMainController;

class testSheet{

    public static void main(String[] args) {
        service.IMainController imain = new IMainController();

        imain.load("test.xlsx", XLSX);
        imain.createNewSheet("Test", null, null, null, null, null, null, null);
    }
}