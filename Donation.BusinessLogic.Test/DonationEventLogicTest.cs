using Microsoft.VisualStudio.TestTools.UnitTesting;
using Donation.Domain;
using Donation.BusinessLogic;
using Donation.DataAccess.Interface;
using Moq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

using Donation.Domain;

namespace Donation.BusinessLogic.Test
{
    [TestClass]
    public class DonationEventLogicTest
    {
        DonationEvent donationEvent;
        Mock<IUnitOfWork> mock;
        DonationEventLogic donationEventLogic;

        [TestInitialize]
        public void TestInitialize()
        {
            donationEvent = new DonationEvent()
            {
                Id = 1,
                Name = "EventName",
                ValidityFrom = new DateTime(),
                ValidityUntil = (new DateTime()).AddDays(15),
                Description = "event description",
                Email = "event@donation.com",
                Active = true,
                Phone = "099 111 111"
            };
            mock = new Mock<IUnitOfWork>();
            donationEventLogic = new DonationEventLogic(mock.Object);
        }

        [TestMethod]
        public void CreateANewDonationEventLogic()
        {
            Assert.IsNotNull(donationEvent);
        }

        /*[TestMethod]
        public void AddANewDonationEvent_OK()
        {
            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(false);
            mock.Setup(m => m.DonationEventRepository.Add((It.IsAny<DonationEvent>())));
            mock.Setup(m => m.DonationEventRepository.Save());

            Result<DonationEvent> result = donationEventLogic.Add(donationEvent);

            Assert.AreEqual(donationEvent.Id, 1);
            mock.VerifyAll();
        }*/

        [TestMethod]
        public void AddDonationEventSameName_ERROR()
        {

            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(true);
            Result<DonationEvent> result = donationEventLogic.Add(donationEvent);
            Assert.AreEqual(result.Message, "The event name already exists.");
            mock.VerifyAll();

        }

        [TestMethod]
        public void DeleteDonationEvent_OK()
        {
            DonationEvent eventToRemove = new DonationEvent();
            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(true);
            mock.Setup(m => m.DonationEventRepository.Get((It.IsAny<Func<DonationEvent, bool>>()))).Returns(eventToRemove);

            mock.Setup(m => m.DonationEventRepository.Save());

            Result<DonationEvent> result = donationEventLogic.Delete(donationEvent.Id);

            Assert.AreEqual(result.Message, "The donation event has been removed.");
            mock.VerifyAll();
        }

        [TestMethod]
        public void DeleteDonationEvent_Wrong()
        {
            DonationEvent eventToRemove = new DonationEvent();
            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(false);

            Result<DonationEvent> result = donationEventLogic.Delete(donationEvent.Id);

            Assert.AreEqual(result.Message, "The event does not exist.");
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetDonationEvent_OK()
        {

            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(true);
            mock.Setup(m => m.DonationEventRepository.Get((It.IsAny<Func<DonationEvent, bool>>()))).Returns(donationEvent);
            Result<DonationEvent> result = donationEventLogic.Get(1);

            Assert.AreEqual(result.ResultObject.Id, 1);
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetDonationEvent_Wrong()
        {
            mock.Setup(m => m.DonationEventRepository.Exist((It.IsAny<Func<DonationEvent, bool>>()))).Returns(false);
            Result<DonationEvent> result = donationEventLogic.Get(1);

            Assert.AreEqual(result.Message, "The event does not exist.");
            mock.VerifyAll();
        }

         [TestMethod]
        public void GetAllDonationsEvent_OK()
        {   
            mock.Setup(m => m.DonationEventRepository.GetAll()).Returns(new List<DonationEvent>());

            IEnumerable<DonationEvent> events = donationEventLogic.GetAll();

            Assert.IsNotNull(events);
            mock.VerifyAll();
        }
    }
}