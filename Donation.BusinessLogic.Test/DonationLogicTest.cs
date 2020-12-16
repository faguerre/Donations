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
    public class DonationLogicTest
    {
        Domain.Donation donation;
        User user, userConfirmed;
        Mock<IUnitOfWork> mock;
        DonationLogic donationLogic;

        [TestInitialize]
        public void TestInitialize()
        {
            user = new User() {
                Name = "UserTest1",
                Id = 1
            };

            userConfirmed = new User() {
                Name = "UserConfirmed",
                Id = 2
            };

            donation = new Domain.Donation();
            donation.Id = 1;
            donation.Name = "Donation1";
            donation.CreatorUser = user;
            donation.Description = "Description1";
            donation.State = new State();
            donation.Files = new List<File>();

            mock = new Mock<IUnitOfWork>();
            donationLogic = new DonationLogic(mock.Object);
        }

        [TestMethod]
        public void CreateANewDonationLogic()
        {
            Assert.IsNotNull(donationLogic);
        }

        [TestMethod]
        public void AddANewDonation_OK()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(true);
            mock.Setup(m => m.DonationRepository.Add((It.IsAny<Domain.Donation>())));
            mock.Setup(m => m.DonationRepository.Save());

            Result<Domain.Donation> result = donationLogic.Add(donation);
            
            Assert.AreEqual(donation.Id, 1);
            mock.VerifyAll();
        }

        [TestMethod]
        public void AddInvalidDonationUserDoesNotExist_ERROR()
        {
            mock.Setup(m => m.UserRepository.Exist((It.IsAny<Func<User, bool>>()))).Returns(false);

            Result<Domain.Donation> result = donationLogic.Add(donation);

            Assert.AreEqual(result.Message, "The user selected as creator does not exist.");
            mock.VerifyAll();
        }  

        [TestMethod]
        public void DeleteADonationThatNotExist_ERROR()
        {
            mock.Setup(m => m.DonationRepository.Exist((It.IsAny<Func<Domain.Donation, bool>>()))).Returns(false);

            Result<Domain.Donation> result = donationLogic.Delete(donation.Id);

            Assert.IsNull(result.ResultObject);
            mock.VerifyAll();
        }  

        [TestMethod]
        public void DeleteAUserThatExist_OK()
        {
            mock.Setup(m => m.DonationRepository.Exist((It.IsAny<Func<Domain.Donation, bool>>()))).Returns(true);
            mock.Setup(m => m.DonationRepository.Delete(donation.Id));
            mock.Setup(m => m.DonationRepository.Save()); 

            Result<Domain.Donation> result = donationLogic.Delete(donation.Id);

            Assert.IsNotNull(result.ResultObject);
            mock.VerifyAll();
        }

        [TestMethod]
        public void GetADonationValid_OK()
        {   
            mock.Setup(m => m.DonationRepository.Exist((It.IsAny<Func<Domain.Donation, bool>>()))).Returns(true);
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);

            Result<Domain.Donation> donationObteined = donationLogic.Get(1);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.ResultObject.Id, donation.Id);
        }

        [TestMethod]
        public void GetADonationThatNotExist_ERROR()
        {
            mock.Setup(m => m.DonationRepository.Exist((It.IsAny<Func<Domain.Donation, bool>>()))).Returns(false);

            Result<Domain.Donation> donationObteined = donationLogic.Get(1);

            Assert.IsNull(donationObteined.ResultObject);
            mock.VerifyAll();
        } 

        [TestMethod]
        public void GetAllDonationsValid_OK()
        {   
            mock.Setup(m => m.DonationRepository.GetAll()).Returns(new List<Domain.Donation>());

            IEnumerable<Domain.Donation> donationObteined = donationLogic.GetAll();

            Assert.IsNotNull(donationObteined);
            mock.VerifyAll();
        }

        [TestMethod]
        public void ExtendDonation_OK()
        {   
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);
            mock.Setup(m => m.DonationRepository.Update(donation.Id, donation));
            mock.Setup(m => m.DonationRepository.Save());

           Result<Domain.Donation> donationObteined = donationLogic.ExtendDonation(donation.Id);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.ResultObject, donation);
        }

        [TestMethod]
        public void ExtendDonationErrorNotFoundDonation_ERROR()
        {   
            Domain.Donation d = null;
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(d);

           Result<Domain.Donation> donationObteined = donationLogic.ExtendDonation(donation.Id);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.Message, "There is not donation related to the id " + donation.Id);
        }

        [TestMethod]
        public void ExtendDonationErrorConfirmedUserWasAlreadyConfigured_ERROR()
        {   
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);

            donation.ConfirmedUser = new User();
            Result<Domain.Donation> donationObteined = donationLogic.ExtendDonation(donation.Id);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.Message, "It is not possible to extend the donation because the donation was already confirmed.");
        }

        [TestMethod]
        public void ExtendDonationWasAlreadyExtended_ERROR()
        {   
            User userAux = null;
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);

           donation.State.WasExtended = true;
           Result<Domain.Donation> donationObteined = donationLogic.ExtendDonation(donation.Id);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.Message, "It is not possible to extend the donation 15 more day because it was previously extended.");
        }
/*
        [TestMethod]
        public void ConfirmDonation_OK()
        {   
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(userConfirmed);
            mock.Setup(m => m.DonationRepository.Save());
            String token = "13251";

           Result<Domain.Donation> donationObteined = donationLogic.ConfirmDonation (donation.Id, token);

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.ResultObject.ConfirmedUser.Id, userConfirmed.Id);
        }
*/
        [TestMethod]
        public void ConfirmDonationErrorNotFoundDonation_ERROR()
        {   
            Domain.Donation d = null;
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(d);
            String token = "13251";

           Result<Domain.Donation> donationObteined = donationLogic.ConfirmDonation (donation.Id, token);

           

            mock.VerifyAll();
            Assert.AreEqual(donationObteined.Message, "There is not donation related to the id " + donation.Id);
        }
/*
        [TestMethod]
        public void ConfirmDonationErrorNotFoundUserConfirmed_ERROR()
        {   
            User userAux = null;
            mock.Setup(m => m.DonationRepository.Get(It.IsAny<Func<Domain.Donation, bool>>())).Returns(donation);
            mock.Setup(m => m.UserRepository.Get(It.IsAny<Func<User, bool>>())).Returns(userAux);
            String token = "13251";

           Result<Domain.Donation> donationObteined = donationLogic.ConfirmDonation (donation.Id, token);
            mock.VerifyAll();
            Assert.AreEqual(donationObteined.Message, "There is not donation related to the id " + userConfirmed.Id);
        }*/
    }
}
