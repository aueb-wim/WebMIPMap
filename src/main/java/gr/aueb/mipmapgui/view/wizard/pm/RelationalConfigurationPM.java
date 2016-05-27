package gr.aueb.mipmapgui.view.wizard.pm;

import it.unibas.spicy.persistence.AccessConfiguration;

public class RelationalConfigurationPM {

    private AccessConfiguration accessConfiguration = new AccessConfiguration();

    public AccessConfiguration getAccessConfiguration() {
        return accessConfiguration;
    }

    public String getDriver() {
        return accessConfiguration.getDriver();
    }

    public void setDriver(String driver) {
        this.accessConfiguration.setDriver(driver);
    }

    public String getUri() {
        return this.accessConfiguration.getUri();
    }

    public void setUri(String uri) {
        this.accessConfiguration.setUri(uri);
    }

    public String getLogin() {
        return this.accessConfiguration.getLogin();
    }

    public void setLogin(String login) {
        this.accessConfiguration.setLogin(login);
    }

    public String getPassword() {
        return this.accessConfiguration.getPassword();
    }

    public void setPassword(String password) {
        this.accessConfiguration.setPassword(password);
    }

    public boolean checkFieldsAccessConfiguration() {
        return (this.getDriver() != null && !this.getDriver().equals("") &&
                this.getUri() != null && !this.getUri().equals("") &&
                this.getLogin() != null && !this.getLogin().equals(""));
    }

}
