using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;

namespace Donation.Domain.Test
{
    [TestClass]
    public class DonationTest
    {

        Donation donation;

        [TestInitialize]
        public void TestInitialize() {
            donation = new Donation();
            donation.Id = 1;
            donation.Name = "Donation1";
            donation.CreatorUser = new User() {
                Name = "UserTest1"
            };
            donation.Description = "Description1";
            donation.State = new State();
            donation.Files = new List<File>();
        }

        [TestMethod]
        public void CreateADonation_OK()
        {
            Assert.IsNotNull(donation);
        }

         [TestMethod]
        public void GetId_OK() {
            Assert.AreEqual(donation.Id, 1);
        }
        [TestMethod]
        public void GetName_OK() {
            Assert.AreEqual(donation.Name, "Donation1");
        }
        [TestMethod]
        public void GetDonationBDoesNotExist_ERROR() {
            Assert.AreNotEqual(donation.Id, 231);
        }

         [TestMethod]
        public void GetDescription_OK() {
            Assert.AreEqual(donation.Description, "Description1");
        }
        [TestMethod]
        public void GetDescriptionDoesNotExist_ERROR() {
            Assert.AreNotEqual(donation.Description, "");
        }
        [TestMethod]
        public void GetState_OK() {
            Assert.AreEqual(donation.State.ActualState, "Created");
        }
        [TestMethod]
        public void GetStateDoesNotExist_ERROR() {
            Assert.AreNotEqual(donation.State.ActualState, "");
        }

        [TestMethod]
        public void GetFilesDate_OK() {
            Assert.IsNotNull(donation.Files);
        }
        
        [TestMethod]
        public void setUserCreator() {
            donation.CreatorUser = new User() {
                Name = "UserTest1"
            };
            Assert.IsNotNull(donation.CreatorUser);
        }
         [TestMethod]
        public void setConfirmedCreator() {
            donation.ConfirmedUser = new User() {
                                Name = "UserTest1"
                                    };
            Assert.IsNotNull(donation.ConfirmedUser);
        }
         [TestMethod]
        public void setAddress() {
            donation.Address = new Address();
            Assert.IsNotNull(donation.Address);
        }

       [TestMethod]
        public void setListTags() {
            donation.DonationTag = new List<DonationTag>();
            Assert.IsNotNull(donation.DonationTag);
        }
    }
}
