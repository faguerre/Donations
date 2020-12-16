using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class UserTest
    {

        User user;

        [TestInitialize]
        public void TestInitialize() {
            user = new User();
            user.Id = 1;
            user.Name = "UserTest";
            user.Email = "usertest@gmail.com";
            user.Phone = 099131313;
            user.Password = "usertest123";
            user.IsExternal = false;
        }

        [TestMethod]
        public void CreateAnEmptyUser_OK()
        {
            Assert.IsNotNull(user);
        }

        [TestMethod]
        public void GetUserById_OK() {
            Assert.AreEqual(user.Id, 1);
        }
        [TestMethod]
        public void GetUserByEmail_OK() {
            Assert.AreEqual(user.Email, "usertest@gmail.com");
        }
        [TestMethod]
        public void GetUserByEmailDoesNotExist_ERROR() {
            Assert.AreNotEqual(user.Email, "usertestgmail.com");
        }

        [TestMethod]
        public void UserComparationByEmail_OK() {
            User userTest2 = new User();
            userTest2.Email = "usertest@gmail.com";
            Assert.AreEqual(user.Email, userTest2.Email);
        }

        [TestMethod]
        public void UserWithNameNonEmpty_OK() {
            Assert.AreEqual(user.Name, "UserTest");
        }

        [TestMethod]
        public void UserWithPassword_OK() {
            Assert.AreEqual(user.Password, "usertest123");
        }

        [TestMethod]
        public void UserWithPhone_OK() {
            Assert.AreEqual(user.Phone, 099131313);
        }
        
        [TestMethod]
        public void createUserNormal_OK() {
            Assert.IsFalse(user.IsExternal);
        }

        [TestMethod]
        public void createUserGoogle_OK() {
            User userGoogle = new User();
            userGoogle.Id = 1;
            userGoogle.Name = "User Google";
            userGoogle.Email = "usertest@gmail.com";
            userGoogle.IsExternal = true;
            Assert.IsTrue(userGoogle.IsExternal);
        }
        [TestMethod]
        public void setListTag_OK()
        {
            List<Domain.UserTag> listTags = new List<Domain.UserTag>();
            user.userTag = listTags;
            Assert.IsNotNull(user.userTag);
        }        
    }
}
