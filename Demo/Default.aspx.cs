using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class _Default : Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        //Sending request for actual weather data
        string appId = "72e1fbb47e61512912d4efb51110b1e7";
        string url = string.Format("http://api.openweathermap.org/data/2.5/forecast/daily?q={0}&units=metric&cnt=1&APPID={1}", "Brno", appId);
        using (System.Net.WebClient client = new System.Net.WebClient())
        {
            string json = client.DownloadString(url);

            WeatherInfo weatherInfo = (new System.Web.Script.Serialization.JavaScriptSerializer()).Deserialize<WeatherInfo>(json);

            lblDescription.Text = weatherInfo.list[0].weather[0].description;
            lblTempMin.Text = string.Format("{0}°С", Math.Round(weatherInfo.list[0].temp.min, 1));
            lblTempMax.Text = string.Format("{0}°С", Math.Round(weatherInfo.list[0].temp.max, 1));
            lblHumidity.Text = weatherInfo.list[0].humidity.ToString();
            tblWeather.Visible = true;

        }
    }


}