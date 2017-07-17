using Microsoft.AspNet.Identity;
using System;
using System.Linq;
using System.Web.UI;
using Demo;

public partial class Account_Register : Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        var manager = new UserManager();
        ApplicationUser user = manager.FindByName("Admin");
        if (user == null)
        {
            user = new ApplicationUser() { UserName = "Admin" };
            user.Email = "alpha@beta.com";
            manager.Create(user, "0123456789");

            user = new ApplicationUser() { UserName = "User" };
            user.Email = "beta@beta.com";
            manager.Create(user, "9876543210");
        }
    }


    protected void CreateUser_Click(object sender, EventArgs e)
    {
        var manager = new UserManager();
        var user = new ApplicationUser() { UserName = UserName.Text };
        user.Email = Email.Text;
        IdentityResult result = manager.Create(user, Password.Text);
        if (result.Succeeded)
        {
            IdentityHelper.SignIn(manager, user, isPersistent: false);
            IdentityHelper.RedirectToReturnUrl(Request.QueryString["ReturnUrl"], Response);
        }
        else
        {
            ErrorMessage.Text = result.Errors.FirstOrDefault();
        }
    }
}