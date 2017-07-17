using Demo;
using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class UsersManagement : Page
{
    ApplicationDbContext db;

    protected void Page_Load(object sender, EventArgs e)
    {
        //Adding two user accounts for Demo, one of them is an admin
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

        //choosing datagrid for two types of user - users and Admin
        if (!Context.User.Identity.GetUserName().Equals("Admin"))
        {
            Grid.Visible = false;
            GridUser.Visible = true;
        }
        else
        {
            Grid.Visible = true;
            GridUser.Visible = false;
        }

        if (!Page.IsPostBack)
        {
            BindData();
        }


    }
    
    /// <summary>
    /// Updating DataGrid
    /// </summary>
    public void BindData()
    {
        db = new ApplicationDbContext();
        List<ApplicationUser> users = db.Users.ToList();
        
        Grid.DataSource = users;
        Grid.DataBind();

        GridUser.DataSource = users;
        GridUser.DataBind();
    }
    protected void Grid_PageIndexChanged(object source, DataGridPageChangedEventArgs e)
    {
        Grid.CurrentPageIndex = e.NewPageIndex;
        GridUser.CurrentPageIndex = e.NewPageIndex;
        BindData();
    }
    protected void Grid_EditCommand(object source, DataGridCommandEventArgs e)
    {
        Grid.EditItemIndex = e.Item.ItemIndex;
        BindData();
    }
    protected void Grid_CancelCommand(object source, DataGridCommandEventArgs e)
    {
        Grid.EditItemIndex = -1;
        BindData();
    }
    protected void Grid_DeleteCommand(object source, DataGridCommandEventArgs e)
    {
        
        string Name = Grid.DataKeys[e.Item.ItemIndex].ToString();
        var manager = new UserManager();
        if (!Name.Equals("Admin"))
        {
            ApplicationUser user = manager.FindByName(Name);
            manager.Delete(user);
            Grid.EditItemIndex = -1;
            BindData();
        }
        else
        {
            ErrorLabel.Text = "You can't delete user Admin!";
            ErrorLabel.Visible = true;
        }
        
    }
    protected void Grid_UpdateCommand(object source, DataGridCommandEventArgs e)
    {
        string Name = Grid.DataKeys[e.Item.ItemIndex].ToString();
        var manager = new UserManager();
        
        ApplicationUser user = manager.FindByName(Name);
        user.Email = ((TextBox)e.Item.Cells[1].Controls[0]).Text;

        manager.Update(user);
        Grid.EditItemIndex = -1;
        BindData();
        
        
    }
    

}