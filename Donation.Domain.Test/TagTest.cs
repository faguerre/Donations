using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class TagTest
    {

        Tag tag;

        [TestInitialize]
        public void TestInitialize()
        {
            tag = new Tag();
        }

        [TestMethod]
        public void CreateAnEmptyTag_OK()
        {
            Assert.IsNotNull(tag);
        }
        [TestMethod]
        public void setNameTag_OK()
        {
            tag.Name = "tagName";
            Assert.AreEqual(tag.Name, "tagName");
        }
        [TestMethod]

        public void setIdTag_OK()
        {
            tag.Id = 1;
            Assert.AreEqual(tag.Id, 1);
        }


    }
}