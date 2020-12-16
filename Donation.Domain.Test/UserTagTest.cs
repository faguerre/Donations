using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class UserTagTest
    {

        UserTag userTag;

        [TestInitialize]
        public void TestInitialize()
        {
            userTag = new UserTag();
        }

        [TestMethod]
        public void CreateAnEmptyUserTag_OK()
        {
            Assert.IsNotNull(userTag);
        }
        
        [TestMethod]
        public void setUserUserTag_OK()
        {
            User user = new User();
            userTag.User = user;

            Assert.IsNotNull(userTag.User);
        }
         [TestMethod]
        public void setTagUserTag_OK()
        {
            Tag tag = new Tag();
            userTag.Tag = tag;

            Assert.IsNotNull(userTag.Tag);
        }
        [TestMethod]

        public void setUserIdUserTag_OK()
        {
            userTag.UserId = 1;
            Assert.AreEqual(userTag.UserId, 1);
        }

       [TestMethod]

        public void setTagIdUserTag_OK()
        {
            userTag.TagId = 1;
            Assert.AreEqual(userTag.TagId, 1);
        }
    }
}