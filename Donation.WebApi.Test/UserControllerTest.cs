using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Donation.WebApi;
using Donation.Domain;
using Donation.BusinessLogic.Interface;


namespace Donation.WebApi.Test
{
    [TestClass]
    public class UserControllerTest
    {

        User _user;
        UserModel userModel;
        IEnumerable<User> listUser;
        Mock<IUserLogic> mock;

        [TestInitialize]
        public void TestInitialize()
        {
                userModel = new UserModel();
                _user = new User();
                mock = new Mock<IUserLogic>(MockBehavior.Strict);   
        }

            [TestMethod]
            public void AddUser_WithParametars_OK()
            {
                mock.Setup(m => m.Add(It.IsAny<User>())); 
                var controller = new UserController();

                IActionResult result = controller.Post(userModel);
                var createdResult = result as OkObjectResult;
                var model = createdResult.Value as Result<User>;

                mock.VerifyAll();
                Assert.IsNotNull(createdResult);
            }
    }
}
