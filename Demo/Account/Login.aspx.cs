using Microsoft.AspNet.Identity;
using Microsoft.Owin.Security;
using System;
using System.Web;
using System.Web.UI;
using Demo;
using Microsoft.AspNet.Identity.EntityFramework;

public partial class Account_Login : Page
{
        protected void Page_Load(object sender, EventArgs e)
        {
            RegisterHyperLink.NavigateUrl = "Register";
            OpenAuthLogin.ReturnUrl = Request.QueryString["ReturnUrl"];
            var returnUrl = HttpUtility.UrlEncode(Request.QueryString["ReturnUrl"]);
            if (!String.IsNullOrEmpty(returnUrl))
            {
                RegisterHyperLink.NavigateUrl += "?ReturnUrl=" + returnUrl;
            }
            //Adding two user accounts for Demo, one of them is an admin
            var manager = new UserManager();
            ApplicationUser user = manager.FindByName("Admin");
            if (user == null)
            {
                user = new ApplicationUser() { UserName = "Admin"};
                user.Email = "alpha@beta.com";
                manager.Create(user, "0123456789");

                user = new ApplicationUser() { UserName = "User" };
                user.Email = "beta@beta.com";
                manager.Create(user, "9876543210");
            }
        }

        protected void LogIn(object sender, EventArgs e)
        {
            if (IsValid)
            {
                // Validate the user password
                var manager = new UserManager();
                ApplicationUser user = manager.Find(UserName.Text, Password.Text);
                if (user != null)
                {
                    IdentityUserRole role = new IdentityUserRole() { RoleId = "peasant" };
                    user.Roles.Add(role);
                    IdentityHelper.SignIn(manager, user, RememberMe.Checked);
                    IdentityHelper.RedirectToReturnUrl(Request.QueryString["ReturnUrl"], Response);
                }
                else
                {
                    FailureText.Text = "Invalid username or password.";
                    ErrorMessage.Visible = true;
                }
            }
        }
}