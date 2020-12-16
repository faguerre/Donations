using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class FileTest
    {

        File file;

        [TestInitialize]
        public void TestInitialize()
        {
            file = new File();
        }

        [TestMethod]
        public void CreateAnEmptyFile()
        {
            Assert.IsNotNull(file);
        }
        [TestMethod]
        public void setName()
        {
            file.Name = "fileName";
            Assert.AreEqual(file.Name, "fileName");
        }
        [TestMethod]

        public void setIdFile()
        {
            file.Id = 1;
            Assert.AreEqual(file.Id, 1);
        }
   
         [TestMethod]
        public void setSizeFile()
        {
            file.Size="12";
            Assert.AreEqual(file.Size, "12");
        }
        [TestMethod]
        public void setDataFiles()
        {
            file.DataFiles = new  byte[1];
            Assert.IsNotNull(file.DataFiles);
        }

    }
}