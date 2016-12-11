/* Copyright 2015-2016 by the Athens University of Economics and Business (AUEB).
   This file is part of WebMIPMap.
   WebMIPMap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    WebMIPMap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with WebMIPMap.  If not, see <http://www.gnu.org/licenses/>.
 */

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
