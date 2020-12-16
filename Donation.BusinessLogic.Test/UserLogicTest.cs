using Microsoft.VisualStudio.TestTools.UnitTesting;
using Donation.Domain;
using Donation.BusinessLogic;
using Donation.DataAccess.Interface;
using Moq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

namespace Donation.BusinessLogic.Test
{
    [TestClass]
    public class UserLogicTest
    {
        User user;
        Mock<IUnitOfWork> mock;
        UserLogic userLogic;

        [TestInitialize]
        public void TestInitialize()
        {
            user = new User();
            user.Id = 1;
            user.Name = "UserTest";
            user.Email = "usertest@gmail.com";
            user.Phone = 099131313;
            user.Password = "usertest123";

            mock = new Mock<IUnitOfWork>();
            userLogic = new UserLogic(mock.Object);
        }

        [TestMethod]
        public void CreateANewUserLogic()
        {
            Assert.IsNotNull(userLogic);
        }

        [TestMethod]
        public void AddANewUserByForm_OK()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false);
            mock.Setup(m => m.UserRepository.Add((It.IsAny<User>())));
            mock.Setup(m => m.UserRepository.Save());

            userLogic.Add(user);
            
            Assert.AreEqual(user.Id, 1);
            mock.VerifyAll();
        }

         [TestMethod]
        public void AddANewUserGoogle_OK()
        {
            User userGoogle = new User();
            userGoogle.Id = 2;
            userGoogle.Name = "UserTest";
            userGoogle.Email = "usertest@gmail.com";

            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false);
            mock.Setup(m => m.UserRepository.Add((It.IsAny<User>())));
            mock.Setup(m => m.UserRepository.Save());

            userLogic.Add(userGoogle);
            
            Assert.AreEqual(userGoogle.Id, 2);
            mock.VerifyAll();
        }

        [TestMethod]
        public void AddInvalidUserTheEmailAlreadyExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);

            Result<User> result = userLogic.Add(user);

            Assert.AreEqual(result.Message, "It is not possible to create an user because the Email is already used.");
            mock.VerifyAll();
        }  

        [TestMethod]
        public void UpdateAUser_OK()
        {   
            user.Name = "NewNameToTest";

            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.UserRepository.Update(user.Id, user));
            mock.Setup(m => m.UserRepository.Save());

            Result<User> result = userLogic.Update(user.Id, user);

            mock.VerifyAll();
            Assert.AreEqual(user.Name, "NewNameToTest");

        }

        [TestMethod]
        public void UpdateAUserThatNotExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false); 

            Result<User> result = userLogic.Update(user.Id, user);

            Assert.AreEqual(result.Message, "It is not possible to update the user "+ user.Id +" because the id does not exist.");
            mock.VerifyAll();
        } 

     /*   [TestMethod]
        public void UpdateAUserThatExistButEmailExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.UserRepository.Get((It.IsAny<Func<User, bool>>()))).Returns(new User());
            Result<User> result = userLogic.Update(user.Id, user);

            Assert.AreEqual(result.Message, "It is not possible to update the user "+user.Id+" because the new email has already been used.");
            mock.VerifyAll();
        }     
*/


        [TestMethod]
        public void DeleteAUserThatNotExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false);

            Result<User> result = userLogic.Delete(user.Id);

            Assert.IsNull(result.ResultObject);
            mock.VerifyAll();
        }  

        [TestMethod]
        public void DeleteAUserThatExist_OK()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.UserRepository.Delete(user.Id));
            mock.Setup(m => m.UserRepository.Save()); 

            Result<User> result = userLogic.Delete(user.Id);

            Assert.IsNotNull(result.ResultObject);
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetAUserValid_OK()
        {   
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);

            Result<User> userObteined = userLogic.Get(1);

            mock.VerifyAll();
            Assert.AreEqual(userObteined.ResultObject.Id, user.Id);
        }

        [TestMethod]
        public void GetAUserThatNotExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false);

            Result<User> userObteined = userLogic.Get(1);

            Assert.IsNull(userObteined.ResultObject);
            mock.VerifyAll();
        } 

         [TestMethod]
        public void GetAllUsersValid_OK()
        {   
            mock.Setup(m => m.UserRepository.GetAll()).Returns(new List<User>());

            IEnumerable<User> usersObteined = userLogic.GetAll();

            Assert.IsNotNull(usersObteined);
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetAllUsersWhenThereAreNotAvailables_ERROR()
        {
            mock.Setup(m => m.UserRepository.GetAll()).Returns(new List<User>());

            IEnumerable<User> usersObteined = userLogic.GetAll();

            Assert.IsNotNull(usersObteined);
            mock.VerifyAll();
        } 

    }
}
