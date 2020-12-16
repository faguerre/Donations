using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;

namespace Donation.Domain.Test
{
    [TestClass]
    public class CenterTest
    {
        Center center;

        
        [TestInitialize]
        public void TestInitialize()
        {
            center = new Center();
            center.Id =1;
            center.Name ="Center 1";
            center.Address="Yi 1436";
        }

         [TestMethod]
        public void CreateACenter_OK()
        {
            Assert.IsNotNull(center);
        }

         [TestMethod]
        public void GetId_OK() {
            Assert.AreEqual(center.Id, 1);
        }

        [TestMethod]
        public void GetName_OK() {
            Assert.AreEqual(center.Name, "Center 1");
        }
        [TestMethod]
        public void GetDCenterDoesNotExist_ERROR() {
            Assert.AreNotEqual(center.Id, 2);
        }
        [TestMethod]
        public void GetName_Wrong() {
            Assert.AreNotEqual(center.Name, "Center 11");
        }
         [TestMethod]
        public void GetAddress_OK() {
            Assert.AreEqual(center.Address, "Yi 1436");
        }
      
    }
}