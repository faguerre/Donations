using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace Donation.DataAccess.Migrations
{
    public partial class DonationDB : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "State",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ActualState = table.Column<string>(nullable: true),
                    CreationDate = table.Column<DateTime>(nullable: false),
                    FinishDate = table.Column<DateTime>(nullable: false),
                    WasExtended = table.Column<bool>(nullable: false),
                    DaysLeft = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_State", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Tag",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Tag", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "User",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    Email = table.Column<string>(nullable: true),
                    Password = table.Column<string>(nullable: true),
                    Phone = table.Column<int>(nullable: false),
                    IsExternal = table.Column<bool>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_User", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "DonationEvents",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    ValidityFrom = table.Column<DateTime>(nullable: false),
                    ValidityUntil = table.Column<DateTime>(nullable: false),
                    Description = table.Column<string>(nullable: true),
                    Active = table.Column<bool>(nullable: false),
                    Email = table.Column<string>(nullable: true),
                    Phone = table.Column<string>(nullable: true),
                    CreatorUserId = table.Column<int>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DonationEvents", x => x.Id);
                    table.ForeignKey(
                        name: "FK_DonationEvents_User_CreatorUserId",
                        column: x => x.CreatorUserId,
                        principalTable: "User",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Sessions",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserId = table.Column<int>(nullable: true),
                    Token = table.Column<Guid>(nullable: false),
                    CreateDate = table.Column<DateTime>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Sessions", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Sessions_User_UserId",
                        column: x => x.UserId,
                        principalTable: "User",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "UserTag",
                columns: table => new
                {
                    UserId = table.Column<int>(nullable: false),
                    TagId = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_UserTag", x => new { x.UserId, x.TagId });
                    table.ForeignKey(
                        name: "FK_UserTag_Tag_TagId",
                        column: x => x.TagId,
                        principalTable: "Tag",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_UserTag_User_UserId",
                        column: x => x.UserId,
                        principalTable: "User",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Address",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    Latitute = table.Column<string>(nullable: true),
                    Longitute = table.Column<string>(nullable: true),
                    DonationEventId = table.Column<int>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Address", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Address_DonationEvents_DonationEventId",
                        column: x => x.DonationEventId,
                        principalTable: "DonationEvents",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Donation",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    CreatorUserId = table.Column<int>(nullable: true),
                    ConfirmedUserId = table.Column<int>(nullable: true),
                    AddressId = table.Column<int>(nullable: true),
                    Description = table.Column<string>(nullable: true),
                    StateId = table.Column<int>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Donation", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Donation_Address_AddressId",
                        column: x => x.AddressId,
                        principalTable: "Address",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Donation_User_ConfirmedUserId",
                        column: x => x.ConfirmedUserId,
                        principalTable: "User",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Donation_User_CreatorUserId",
                        column: x => x.CreatorUserId,
                        principalTable: "User",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Donation_State_StateId",
                        column: x => x.StateId,
                        principalTable: "State",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "DonationTag",
                columns: table => new
                {
                    DonationId = table.Column<int>(nullable: false),
                    TagId = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DonationTag", x => new { x.DonationId, x.TagId });
                    table.ForeignKey(
                        name: "FK_DonationTag_Donation_DonationId",
                        column: x => x.DonationId,
                        principalTable: "Donation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_DonationTag_Tag_TagId",
                        column: x => x.TagId,
                        principalTable: "Tag",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "File",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(nullable: true),
                    Size = table.Column<string>(nullable: true),
                    DataFiles = table.Column<byte[]>(nullable: true),
                    DonationId = table.Column<int>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_File", x => x.Id);
                    table.ForeignKey(
                        name: "FK_File_Donation_DonationId",
                        column: x => x.DonationId,
                        principalTable: "Donation",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Address_DonationEventId",
                table: "Address",
                column: "DonationEventId");

            migrationBuilder.CreateIndex(
                name: "IX_Donation_AddressId",
                table: "Donation",
                column: "AddressId");

            migrationBuilder.CreateIndex(
                name: "IX_Donation_ConfirmedUserId",
                table: "Donation",
                column: "ConfirmedUserId");

            migrationBuilder.CreateIndex(
                name: "IX_Donation_CreatorUserId",
                table: "Donation",
                column: "CreatorUserId");

            migrationBuilder.CreateIndex(
                name: "IX_Donation_StateId",
                table: "Donation",
                column: "StateId");

            migrationBuilder.CreateIndex(
                name: "IX_DonationEvents_CreatorUserId",
                table: "DonationEvents",
                column: "CreatorUserId");

            migrationBuilder.CreateIndex(
                name: "IX_DonationTag_TagId",
                table: "DonationTag",
                column: "TagId");

            migrationBuilder.CreateIndex(
                name: "IX_File_DonationId",
                table: "File",
                column: "DonationId");

            migrationBuilder.CreateIndex(
                name: "IX_Sessions_UserId",
                table: "Sessions",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_UserTag_TagId",
                table: "UserTag",
                column: "TagId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "DonationTag");

            migrationBuilder.DropTable(
                name: "File");

            migrationBuilder.DropTable(
                name: "Sessions");

            migrationBuilder.DropTable(
                name: "UserTag");

            migrationBuilder.DropTable(
                name: "Donation");

            migrationBuilder.DropTable(
                name: "Tag");

            migrationBuilder.DropTable(
                name: "Address");

            migrationBuilder.DropTable(
                name: "State");

            migrationBuilder.DropTable(
                name: "DonationEvents");

            migrationBuilder.DropTable(
                name: "User");
        }
    }
}
