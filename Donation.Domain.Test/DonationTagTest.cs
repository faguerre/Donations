using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class DonationTagTest
    {

        DonationTag donationTag;

        [TestInitialize]
        public void TestInitialize()
        {
            donationTag = new DonationTag();
        }

        [TestMethod]
        public void CreateAnEmptyDonationTag()
        {
            Assert.IsNotNull(donationTag);
        }
        
        [TestMethod]
        public void setDonationId()
        {
            donationTag.DonationId=1;
            Assert.AreEqual(   donationTag.DonationId,1);
        }
          [TestMethod]
        public void setTagId()
        {
            donationTag.TagId=1;
            Assert.AreEqual(   donationTag.TagId,1);
        }

         [TestMethod]
        public void setDonation()
        {
            donationTag.Donation = new Donation();
            Assert.IsNotNull(donationTag.Donation);
        }
           [TestMethod]
        public void setTag()
        {
            donationTag.Tag= new Tag();
            Assert.IsNotNull(donationTag.Tag);
        }
           }
}