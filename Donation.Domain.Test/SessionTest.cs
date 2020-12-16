using Microsoft.VisualStudio.TestTools.UnitTesting;
using  System;

namespace Donation.Domain.Test
{
    [TestClass]
    public class SessionTest
    {

        Session session;

        [TestInitialize]
        public void TestInitialize()
        {
            session = new Session();
        }

        [TestMethod]
        public void CreateAnEmptySession_OK()
        {
            Assert.IsNotNull(session);
        }

        [TestMethod]
        public void setIdTag_OK()
        {
            session.Id = 1;
            Assert.AreEqual(session.Id , 1);
        }

          
        [TestMethod]
        public void setUserSession_OK()
        {
            User user = new User();
            session.User = user;

            Assert.IsNotNull(session.User);
        }
        [TestMethod]
        public void setTokenSession_OK()
        {
            Guid guid = new Guid();
            session.Token = guid;
            Assert.AreEqual(session.Token , guid);
        }

        [TestMethod]
        public void setCreatorDateSession_OK()
        {
            DateTime  date = DateTime.Now;
            session.CreateDate = date;
            Assert.AreEqual(session.CreateDate , date);
        }
    }
}