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
 
];

//1) Calculation: TOTAL MARKS

function calculateTotalMarks(student) {
  let total = 0;
  for (let sub of student.marks) {
    total += sub.score;
  }
  return total;
}
//console.log("Total marks scored by Lalit:", calculateTotalMarks(students[0]));
//console.log("Total marks scored by Rahul:", calculateTotalMarks(students[1]));

//2)Calculation: AVERAGE MARKS

function calculateAverageMarks(student){
    const total = calculateTotalMarks(student);
    
    return total/student.marks.length;
}
//console.log("Avg marks scored by Lalit:", calculateAverageMarks(students[0]));
//console.log("Avg marks scored by Rahul:", calculateAverageMarks(students[1]));



//3)Calculating : Grades Logic
function Grade(student) {

  let avg =calculateAverageMarks(student);
  if (avg >= 85) {
    return "A";
  } else if (avg >= 70) {
    return "B";
  } else if (avg >= 50) {
    return "C";
  } else {
    return "Fail";
  }
}
//console.log("Lalit Grade:", Grade(students[0]));
//console.log("Rahul Grade:", Grade(students[1]));


//4)calculating:SUBJECT-WISE HIGHEST Marks
