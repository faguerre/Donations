using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Donation.Domain.Test
{
    [TestClass]
    public class ResultTest
    {

        Result<User> resultTest;

        [TestInitialize]
        public void TestInitialize()
        {
            resultTest = new Result<User>();
        }

        [TestMethod]
        public void CreateAnEmptyResult()
        {
            Assert.IsNotNull(resultTest);
        }

        [TestMethod]
        public void setMessage()
        {
            resultTest.Message = "message";
            Assert.AreEqual(resultTest.Message, "message");
        }
        [TestMethod]

        public void setObject()
        {
             resultTest = new Result<User>();
            resultTest.ResultObject  =new User();
            Assert.IsNotNull(resultTest.ResultObject );
        }
   
    }
}