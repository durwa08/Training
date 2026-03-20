const students = [
  {
    name: "Lalit",
    marks: [
      { subject: "Math", score: 78 },
      { subject: "English", score: 82 },
      { subject: "Science", score: 74 },
      { subject: "History", score: 69 },
      { subject: "Computer", score: 88 }
    ],
    attendance: 82
  },
  {
    name: "Rahul",
    marks: [
      { subject: "Math", score: 90 },
      { subject: "English", score: 85 },
      { subject: "Science", score: 80 },
      { subject: "History", score: 76 },
      { subject: "Computer", score: 92 }
    ],

    attendance: 91
  },
  {
    name:"Aman",
    marks: [
      { subject: "Math", score: 35 },
      { subject: "English", score: 60 },
      { subject: "Science", score: 55 },
      { subject: "History", score: 50 },
      { subject: "Computer", score: 65 }
    ],
    attendance: 70
  }
];

// Calculation of total marks of Students 
function calculateTotalMarks(student) {
  let total = 0;
  for (let sub of student.marks) {
    total += sub.score;
  }
  return total;
}
console.log("Total marks scored by Lalit:", calculateTotalMarks(students[0]));
console.log("Total marks scored by Rahul:", calculateTotalMarks(students[1]));

//Calculating Average Marks
