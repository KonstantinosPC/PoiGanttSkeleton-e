package service;

public class MainControllerFactory {

	public IMainController createMainController() {
		return (new MainController());
	}
}
