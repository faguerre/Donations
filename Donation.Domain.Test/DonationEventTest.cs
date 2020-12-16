using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;


namespace Donation.Domain.Test
{
    [TestClass]
    public class DonationEventTest
    {
        DonationEvent donationEvent;
        String donationEventString;
        User creatorUser = new User();

        [TestInitialize]
        public void TestInitialize()
        {
            donationEvent = new DonationEvent();
            donationEvent.Id = 1;
            donationEventString = "Navidad en pandemia";
            donationEvent.ValidityFrom = DateTime.Now;
            donationEvent.ValidityUntil = (donationEvent.ValidityFrom).AddDays(15);
            donationEvent.Name = donationEventString;
            donationEvent.Description = "description";
            donationEvent.Active = true;
            donationEvent.Email = "donaciones@donaciones.com";
            donationEvent.Phone = "098123456";

            creatorUser.Name = "user";
            donationEvent.CreatorUser = creatorUser;

        }

        [TestMethod]
        public void CreateADonationEvent_OK()
        {
            Assert.IsNotNull(donationEvent);
        }


        [TestMethod]
        public void CreateADonationEventId_OK()
        {
            Assert.IsTrue(donationEvent.Id == 1);
        }

        [TestMethod]
        public void CreateADonationEventId_Wrong()
        {
            Assert.IsFalse(donationEvent.Id == 2);
        }

        [TestMethod]
        public void CreateADonationEventName_OK()
        {
            donationEvent.Name = "Name";
            Assert.IsNotNull(donationEvent.Name);
        }

        [TestMethod]
        public void SetDonationEventName_OK()
        {
            Assert.AreEqual(donationEvent.Name, donationEventString);
        }

        [TestMethod]
        public void SetDonationEventName_Wrong()
        {
            Assert.AreNotEqual(donationEvent.Name, "Name");
        }

        [TestMethod]
        public void IsValidName_OK()
        {
            Assert.IsTrue(donationEvent.IsValid());
        }

        [TestMethod]
        public void IsValidName_Wrong()
        {
            donationEvent.Name = "";
            Assert.IsFalse(donationEvent.IsValid());
        }

        [TestMethod]
        public void CreateValidityFrom_OK()
        {
            Assert.IsNotNull(donationEvent.ValidityFrom);
        }

        [TestMethod]
        public void IsValidValidityFrom_OK()
        {
            Assert.IsTrue(donationEvent.IsValid());
        }
        [TestMethod]
        public void CreateValidityUntil_OK()
        {
            Assert.IsNotNull(donationEvent.ValidityUntil);
        }

        [TestMethod]
        public void IsValidValidityFrom_Wrong()
        {
            donationEvent.ValidityUntil = (DateTime.Now).AddDays(-1);
            Assert.IsFalse(donationEvent.IsValid());
        }
        [TestMethod]
        public void CreateValidDescription_OK()
        {
            Assert.IsNotNull(donationEvent.Description);
        }

        [TestMethod]
        public void getValidDescription_OK()
        {
            Assert.AreEqual(donationEvent.Description, "description");
        }
        [TestMethod]
        public void IsValidDescription_OK()
        {
            Assert.IsTrue(donationEvent.IsValid());
        }

        [TestMethod]
        public void IsValidDescription_Wrong()
        {
            donationEvent.Description = "";
            Assert.IsFalse(donationEvent.IsValid());
        }

        [TestMethod]
        public void CreateActive_OK()
        {
            Assert.IsTrue(donationEvent.Active);
        }

        [TestMethod]
        public void CreateInactive_OK()
        {
            donationEvent.Active = false;
            Assert.IsFalse(donationEvent.Active);
        }

        [TestMethod]
        public void CreateADonationEventEMail_OK()
        {
            Assert.IsNotNull(donationEvent.Email);
        }


        [TestMethod]
        public void IsValidDonationEventEMail_OK()
        {
            Assert.IsTrue(donationEvent.IsValid());
        }


        [TestMethod]
        public void IsValidDonationEventEMail_Wrong()
        {
            donationEvent.Email = "";
            Assert.IsFalse(donationEvent.IsValid());
        }

        [TestMethod]
        public void IsValidEmail_wrong()
        {
            donationEvent.Email = "notAEmail";
            Assert.IsFalse(donationEvent.IsValid());
        }
       
    

        [TestMethod]
        public void CreateADonationEventPhone_OK()
        {
            Assert.AreEqual(donationEvent.Phone, "098123456");
        }

        [TestMethod]
        public void IsValidPhone_wrong()
        {
            donationEvent.Phone = "notAPhone";
            Assert.IsFalse(donationEvent.IsValid());
        }
    
        [TestMethod]
        public void IsValidPhone_OK()
        {

            Assert.IsTrue(donationEvent.IsValid());
        }
        [TestMethod]
        public void GetCenters_OK()
        {
            Assert.IsNotNull(donationEvent.Centers);
        }

        [TestMethod]
        public void AddCenter_OK()
        {
            Domain.Address address = new Domain.Address();
            address.Name = "centro1";
            address.Latitute = "-35.2498554";
            address.Longitute = "-56.6407549";
            address.Id = 1;
            donationEvent.Centers.Add(address);
            Assert.IsTrue(donationEvent.Centers.Count > 0);
        }

        [TestMethod]
        public void GetCenter_OK()
        {
            Domain.Address address = new Domain.Address();
            address.Name = "centro1";
            address.Latitute = "-35.2498554";
            address.Longitute = "-56.6407549";
            donationEvent.Centers.Add(address);

            Assert.IsNotNull(donationEvent.Centers.Find(x => x.Name.Equals("centro1")));
        }

        [TestMethod]
        public void RemoveCenter_OK()
        {
            Domain.Address address = new Domain.Address();
            address.Name = "centro1";
            address.Latitute = "-35.2498554";
            address.Longitute = "-56.6407549";
            donationEvent.Centers.Add(address);

            donationEvent.Centers.Remove(address);
            Assert.IsTrue(donationEvent.Centers.Count == 0);
        }

        [TestMethod]
        public void RemoveCenter_Wrong()
        {
            Domain.Address address = new Domain.Address();
            address.Name = "centro1";
            address.Latitute = "-35.2498554";
            address.Longitute = "-56.6407549";
            donationEvent.Centers.Add(address);

            Domain.Address address2 = new Domain.Address();
            address2.Name = "centrow";
            address2.Latitute = "-35.2498554";
            address2.Longitute = "-56.6407549";
            donationEvent.Centers.Add(address2);


            donationEvent.Centers.Add(address);
            donationEvent.Centers.Remove(address2);
            Assert.IsTrue(donationEvent.Centers.Count > 0);
        }
        [TestMethod]
        public void isActive_OK()
        {
            Assert.IsTrue(donationEvent.IsActive());
        }

        [TestMethod]
        public void isNotActive_OK()
        {
            donationEvent.Active = false;
            Assert.IsFalse(donationEvent.IsActive());
        }
        [TestMethod]
        public void isActiveFrom_OK()
        {
            Assert.IsTrue(donationEvent.IsActive());
        }

        [TestMethod]
        public void isNotActiveFrom_OK()
        {
            donationEvent.ValidityFrom = (donationEvent.ValidityFrom).AddDays(1);

            Assert.IsFalse(donationEvent.IsActive());
        }

        [TestMethod]
        public void isActiveUntil_OK()
        {
            Assert.IsTrue(donationEvent.IsActive());
        }

        [TestMethod]
        public void isNotActiveUntil_OK()
        {
            donationEvent.ValidityFrom = DateTime.Now.AddDays(-2);
            donationEvent.ValidityUntil = (donationEvent.ValidityFrom).AddDays(1);

            Assert.IsFalse(donationEvent.IsActive());
        }
        [TestMethod]
        public void verifyUser_OK()
        {
            donationEvent.CreatorUser = creatorUser;
            Assert.IsNotNull(donationEvent.CreatorUser);
        }

        [TestMethod]
        public void IsValidUser_OK()
        {

            Assert.IsTrue(donationEvent.IsValid());
        }

        [TestMethod]
        public void IsValidUser_Wrong()
        {
            donationEvent.CreatorUser = null;
            Assert.IsFalse(donationEvent.IsValid());
        }

        [TestMethod]
        public void isValidEvent(){
            donationEvent = new DonationEvent();
            donationEvent.Id = 1;
            donationEventString = "Navidad en pandemia";
            donationEvent.ValidityFrom = DateTime.Now;
            donationEvent.ValidityUntil = (donationEvent.ValidityFrom).AddDays(15);
            donationEvent.Name = donationEventString;
            donationEvent.Description = "description";
            donationEvent.Active = true;
            donationEvent.Email = "donaciones@donaciones.com";
            donationEvent.Phone = "098123456";

            creatorUser.Name = "user";
            donationEvent.CreatorUser = creatorUser;

            Assert.IsTrue(donationEvent.IsValid());
        }   
[TestMethod]
        public void isNotValidEventPhone(){
            donationEvent = new DonationEvent();
            donationEvent.Id = 1;
            donationEventString = "Navidad en pandemia";
            donationEvent.ValidityFrom = DateTime.Now;
            donationEvent.ValidityUntil = (donationEvent.ValidityFrom).AddDays(15);
            donationEvent.Name = donationEventString;
            donationEvent.Description = "description";
            donationEvent.Active = true;
            donationEvent.Email = "donaciones@donaciones.com";
            donationEvent.Phone = "asda";

            creatorUser.Name = "user";
            donationEvent.CreatorUser = creatorUser;

            Assert.IsFalse(donationEvent.IsValid());
        }

        [TestMethod]
        public void isNotValidEventEmail(){
            donationEvent = new DonationEvent();
            donationEvent.Id = 1;
            donationEventString = "Navidad en pandemia";
            donationEvent.ValidityFrom = DateTime.Now;
            donationEvent.ValidityUntil = (donationEvent.ValidityFrom).AddDays(15);
            donationEvent.Name = donationEventString;
            donationEvent.Description = "description";
            donationEvent.Active = true;
            donationEvent.Email = "invalid email";
            donationEvent.Phone = "098123456";

            creatorUser.Name = "user";
            donationEvent.CreatorUser = creatorUser;


            Assert.IsFalse(donationEvent.IsValid());
        }   

    }
}