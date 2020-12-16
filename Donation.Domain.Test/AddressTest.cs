using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace Donation.Domain.Test
{
    [TestClass]
    public class AddressTest
    {

        Address address;

        [TestInitialize]
        public void TestInitialize() {
            address = new Address();
            address.Latitute = "-35.2498554";
            address.Longitute = "-56.6407549";
            address.Name = "Hector Gutierrez Ruiz 1229";
            address.Id = 1;
        }

        [TestMethod]
        public void CreateAnEmptyAddress_OK()
        {
            Assert.IsNotNull(address);
        }

        [TestMethod]
        public void GetAddressById_OK() {
            Assert.AreEqual(address.Id, 1);
        }
        [TestMethod]
        public void GetLatitute_OK() {
            Assert.AreEqual(address.Latitute, "-35.2498554");
        }
        [TestMethod]
        public void GetLatituteWrong() {
            Assert.AreNotEqual(address.Latitute, "");
        }

        public void GetLongitute_OK() {
            Assert.AreEqual(address.Longitute, "-56.6407549");
        }
        [TestMethod]
        public void GetLongituteWrong() {
            Assert.AreNotEqual(address.Longitute, "");
        }

        public void GetName_OK() {
            Assert.AreEqual(address.Name, "Hector Gutierrez Ruiz 1229");
        }
        [TestMethod]
        public void GetNameWrong() {
            Assert.AreNotEqual(address.Name, "");
        }
    }
}