<%@ Page Title="About" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeFile="UsersManagement.aspx.cs" Inherits="UsersManagement" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">
    <h2>Users Managements</h2>
    <p>Here you can overview all registered users. If you are an administrator, you can delete any user account (except yours account) or change email address of any user. 
        If you want to add a user, you will have to register him/ her (I want you to try registration process).</p>
    <div>
            <asp:DataGrid ID="Grid" runat="server" PageSize="5" AllowPaging="True" DataKeyField="UserName"
            AutoGenerateColumns="False" CellPadding="4" ForeColor="#0000ff" GridLines="None" width="100%"
            OnPageIndexChanged="Grid_PageIndexChanged" OnCancelCommand="Grid_CancelCommand"
            OnDeleteCommand="Grid_DeleteCommand" OnEditCommand="Grid_EditCommand" OnUpdateCommand="Grid_UpdateCommand">
            <Columns>
            <asp:BoundColumn HeaderText="UserName" DataField="UserName" ReadOnly="true">
            </asp:BoundColumn>
            <asp:BoundColumn DataField="Email" HeaderText="Email">
            </asp:BoundColumn>
            <asp:EditCommandColumn EditText="Edit" CancelText="Cancel" UpdateText="Update" HeaderText="Edit" >
            </asp:EditCommandColumn>
            <asp:ButtonColumn CommandName="Delete" HeaderText="Delete" Text="Delete">
            </asp:ButtonColumn>
            </Columns>
            <FooterStyle BackColor="#000000" Font-Bold="True" ForeColor="Black" />
            <SelectedItemStyle BackColor="#D3D3D3" Font-Bold="True" ForeColor="Black" />
            <PagerStyle BackColor="#D3D3D3" ForeColor="Black" HorizontalAlign="Center" Mode="NumericPages" />
            <AlternatingItemStyle BackColor="White" />
            <ItemStyle BackColor="#C0C0C0" ForeColor="Black" />
            <HeaderStyle BackColor="#000000" Font-Bold="True" ForeColor="White" />
            </asp:DataGrid>
            

            <asp:DataGrid ID="GridUser" runat="server" PageSize="5" AllowPaging="True" DataKeyField="UserName"
            AutoGenerateColumns="False" CellPadding="4" ForeColor="#0000ff" GridLines="None" width="100%"
            OnPageIndexChanged="Grid_PageIndexChanged">
            <Columns>
            <asp:BoundColumn HeaderText="UserName" DataField="UserName">
            </asp:BoundColumn>
            <asp:BoundColumn DataField="Email" HeaderText="Email">
            </asp:BoundColumn>
            </Columns>
            <FooterStyle BackColor="#000000" Font-Bold="True" ForeColor="Black" />
            <SelectedItemStyle BackColor="#D3D3D3" Font-Bold="True" ForeColor="Black" />
            <PagerStyle BackColor="#D3D3D3" ForeColor="Black" HorizontalAlign="Center" Mode="NumericPages" />
            <AlternatingItemStyle BackColor="White" />
            <ItemStyle BackColor="#C0C0C0" ForeColor="Black" />
            <HeaderStyle BackColor="#000000" Font-Bold="True" ForeColor="White" />
            </asp:DataGrid>

            <p> 
                <asp:Label ID="ErrorLabel" Text="" ForeColor="Red" runat="server" Visible="false" HorizontalAlign="Center">

                </asp:Label>

            </p>
    </div>

</asp:Content>
