using Microsoft.EntityFrameworkCore;
using Donation.Domain;
using System;


namespace Donation.DataAccess
{
    public class DonationContext : DbContext
    {
        public DbSet<User> User { get; set; }
        public DbSet<Domain.Donation> Donation { get; set; }
        public DbSet<Session> Sessions { get; set; }
        public DbSet<DonationEvent> DonationEvents { get; set; }
        public DbSet<Tag> Tag { get; set; }
        public DonationContext(DbContextOptions<DonationContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<UserTag>()
                .HasKey(c => new { c.UserId, c.TagId });

            modelBuilder.Entity<DonationTag>()
                .HasKey(c => new { c.DonationId, c.TagId });
        }
    }
}