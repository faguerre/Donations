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
    public class SessionLogicTest
    {
        User user;
        Mock<IUnitOfWork> mock;
        UserLogic userLogic;
        Session session;
        SessionLogic sessionLogic;

        [TestInitialize]
        public void TestInitialize()
        {
            user = new User();
            user.Id = 1;
            user.Name = "UserTest";
            user.Email = "usertest@gmail.com";
            user.Phone = 099131313;
            user.Password = "usertest123";
            user.IsExternal = false;

            session = new Session();
            session.Id = 1;
            session.User = user;
            session.Token = Guid.NewGuid();
            session.CreateDate = DateTime.Now;

            mock = new Mock<IUnitOfWork>();
            userLogic = new UserLogic(mock.Object);
            sessionLogic = new SessionLogic(mock.Object);
        }

        [TestMethod]
        public void GetLoginTest()
        {
            mock.Setup(m => m.UserRepository.Exist(It.IsAny<Func<User, bool>>())).Returns(true);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);
            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(false);
            mock.Setup(m => m.SessionRepository.Add((It.IsAny<Session>())));
            mock.Setup(m => m.SessionRepository.Save());

            Result<Session> sessionObteined = sessionLogic.Login(user.Email, user.Password);

            mock.VerifyAll();
            Assert.AreEqual(session.Id, 1);
        }
         [TestMethod]
        public void GetLoginGoogleTest()
        {
            user.IsExternal=true;
            mock.Setup(m => m.UserRepository.Exist(It.IsAny<Func<User, bool>>())).Returns(true);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);
            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(false);
            mock.Setup(m => m.SessionRepository.Add((It.IsAny<Session>())));
            mock.Setup(m => m.SessionRepository.Save());

            Result<Session> sessionObteined = sessionLogic.Login(user.Email, user.Password);

            mock.VerifyAll();
            Assert.AreEqual(session.Id, 1);
        }

        // [TestMethod]
        // public void GetLoginExistTest()
        // {
        //     mock.Setup(m => m.UserRepository.Exist(It.IsAny<Func<User, bool>>())).Returns(true);
        //     mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);
        //     mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(true);
        //     mock.Setup(m => m.SessionRepository.Get(It.IsAny<Func<Session, bool>>())).Returns(session);
        //     mock.Setup(m => m.SessionRepository.Update(session.Id, session));
        //     mock.Setup(m => m.SessionRepository.Save());

        //     Result<Session> sessionObteined = sessionLogic.Login(user.Email, user.Password);

        //     mock.VerifyAll();
        //     Assert.AreEqual(session.Id, 1);
        // }

        [TestMethod]
        public void GetLoginTestBadAdmin()
        {
            mock.Setup(m => m.UserRepository.Exist(It.IsAny<Func<User, bool>>())).Returns(false);

            Result<Session> result = sessionLogic.Login(user.Email, user.Password);

            Assert.AreEqual(result.Message, "Mail or password is not valid.");
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetLoginTestBadPassword()
        {
            string password = "123";
            mock.Setup(m => m.UserRepository.Exist(It.IsAny<Func<User, bool>>())).Returns(true);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);

            Result<Session> result = sessionLogic.Login(user.Email, password);

            Assert.AreEqual(result.Message, "Mail or password is not valid.");
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetSessionFromGuid()
        {
            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(true);
            mock.Setup(m => m.SessionRepository.Get(It.IsAny<Func<Session, bool>>())).Returns(session);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(user);

            Result<User> userObteined = sessionLogic.GetUserFromToken(session.Token);

            mock.VerifyAll();
            Assert.AreEqual(userObteined.ResultObject.Id, user.Id);
        }

        [TestMethod]
        public void ValidToken()
        {

            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(true);

            bool result = sessionLogic.IsValidToken(session.Token);

            mock.VerifyAll();
            Assert.IsTrue(result);
        }

        [TestMethod]
        public void LogoutTest_Ok()
        {
            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(true);
            mock.Setup(m => m.SessionRepository.Get(It.IsAny<Func<Session, bool>>())).Returns(session);
            mock.Setup(m => m.SessionRepository.Delete(session.Id));

            Result<Session> result = sessionLogic.Logout(session.Token);

            Assert.IsNotNull(result.ResultObject);
            mock.VerifyAll();
        }

        public void LogoutTest_ERROR()
        {
            mock.Setup(m => m.SessionRepository.Exist((It.IsAny<Func<Session, bool>>()))).Returns(false);

            Result<Session> result = sessionLogic.Logout(session.Token);

            Assert.AreEqual(result.Message, "It is not possible to delete the session "+session.Token+" because the token does not exist.");
            mock.VerifyAll();
        }
    } 
}