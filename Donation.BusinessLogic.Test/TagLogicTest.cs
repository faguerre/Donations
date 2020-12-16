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
    public class TagLogicTest
    {
        Mock<IUnitOfWork> mock;
        Tag tag;
        TagLogic tagLogic;

        [TestInitialize]
        public void TestInitialize()
        {
            mock = new Mock<IUnitOfWork>();
            tagLogic = new TagLogic(mock.Object);
            tag = new Tag();
            tag.Id = 1;
            tag.Name = "Mueble";
        }
/*
        [TestMethod]
        public void GetTagValid_OK()
        {   
            mock.Setup(m => m.TagRepository.Get(It.IsAny<Func<Tag, bool>>())).Returns(tag);

            Result<Tag> tagObteined = tagLogic.Get(1);

            mock.VerifyAll();
            Assert.AreEqual(tagObteined.ResultObject.Id, tag.Id);
        }
*/
         [TestMethod]
        public void GetAllTagsValid_OK()
        {   
            mock.Setup(m => m.TagRepository.GetAll()).Returns(new List<Tag>());

            IEnumerable<Tag> tagsObteined = tagLogic.GetAll();

            Assert.IsNotNull(tagsObteined);
            mock.VerifyAll();
        }

    } 
}