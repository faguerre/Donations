using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace Donation.Domain.Test
{
    [TestClass]
    public class StateTest
    {

        State state;

        [TestInitialize]
        public void TestInitialize() {
            state = new State();
            state.CreationDate = System.DateTime.Today;
            state.Id = 1;
        }

        [TestMethod]
        public void CreateAnEmptyUser_OK()
        {
            Assert.IsNotNull(state);
        }

        [TestMethod]
        public void GetStateById_OK() {
            Assert.AreEqual(state.Id, 1);
        }
        [TestMethod]
        public void GetActualState_OK() {
            Assert.AreEqual(state.ActualState, "Created");
        }
        [TestMethod]
        public void GetActualStateWrong() {
            Assert.AreNotEqual(state.ActualState, "Finished");
        }

        [TestMethod]
        public void GetCreatedDate_OK() {
            Assert.AreEqual(state.CreationDate, System.DateTime.Today.Date);
        }
        [TestMethod]
        public void GetCreatedDateDoesNotExist_ERROR() {
            Assert.AreNotEqual(state.CreationDate, new DateTime());
        }

        [TestMethod]
        public void GetFinishDate_OK() {
            Assert.AreEqual(state.CreationDate.AddDays(15), state.FinishDate);
        }

        [TestMethod]
        public void GetFinishDateDoesNotExist_ERROR() {
            Assert.AreNotEqual(state.CreationDate, new DateTime());
        }

        [TestMethod]
        public void ChangeState_OK() {
            state.ActualState = "Finished";
            Assert.AreEqual(state.ActualState, "Finished");
        }
       [TestMethod]
        public void verifyWasExtended_OK() {
            state.WasExtended = true;
            Assert.IsTrue(state.WasExtended);
        }
         [TestMethod]
        public void verifyWasNotExtended_OK() {
            state.WasExtended = false;
            Assert.IsFalse(state.WasExtended);
        }
        
        [TestMethod]
        public void verifyDaysLeft_OK() {
            
            Assert.AreEqual(state.DaysLeft,15);
        }
         [TestMethod]
        public void updateStatus_OK() {
            state.UpdateStatus();
            Assert.AreEqual(state.ActualState,"Finished");
        }

        [TestMethod]
        public void updateDayLeft() {
            
            state.UpdateDaysLeft();
            Assert.AreEqual(state.DaysLeft,15);
        }
        
        [TestMethod]
        public void updateDayLeftLess15() {
            DateTime date = System.DateTime.Today.Date;
            state.CreationDate = new DateTime(2020,11,25);
            state.FinishDate = state.CreationDate.AddDays(15);
            state.UpdateDaysLeft();
            Console.WriteLine(state.CreationDate);
            
            Console.WriteLine(state.DaysLeft);
            Assert.AreEqual(state.DaysLeft,14);
        }
    }
}
