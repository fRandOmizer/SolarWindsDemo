<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">

    <div class="jumbotron">
        <h1>Demo for SolarWinds</h1>
        <p class="lead">
            This is a small demonstration of my skills with C# and ASP.NET. Below this text, you can see actual weather forest for Brno. 

        </p>
        <p>
            To fully experience functionality of this product, log in as Admin with password 0123456789. You will get permission to see and update all registered accounts and delete all accounts except yours on page Users Management. 
        </p>
    </div>

    <div class="row">
        <div class="col-md-4">
            <h2>Weather in Brno</h2>
                
                <table id="tblWeather" runat="server" border="0" cellpadding="0" cellspacing="0"
                    visible="false">
                    <tr>
                        <td>
                            
                            <asp:Label ID="lblDescription" runat="server" />
                            
                            
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Humidity:
                            <asp:Label ID="lblHumidity" runat="server" />
                        </td>

                    </tr>
                    <tr>
                        <td>
                            Minimimal temperature:
                            <asp:Label ID="lblTempMin" runat="server" />
                        </td>

                    </tr>
                    <tr>
                        <td>
                            Maximal temperature:
                            <asp:Label ID="lblTempMax" runat="server" />
                        </td>

                    </tr>
                </table>
            
            <p>
                <a class="btn btn-default" href="http://openweathermap.org/">Source &raquo;</a>
            </p>
        </div>
        
    </div>
</asp:Content>
